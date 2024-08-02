package com.ludicomm.presentation.viewmodel

import androidx.compose.ui.graphics.Color.Companion.Blue
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseUser
import com.ludicomm.data.model.BoardGame
import com.ludicomm.data.model.BoardGames
import com.ludicomm.data.model.PlayerMatchData
import com.ludicomm.data.model.SingleBoardGame
import com.ludicomm.data.model.SingleBoardGameList
import com.ludicomm.data.model.User
import com.ludicomm.data.repository.AuthRepository
import com.ludicomm.data.repository.BGGRepository
import com.ludicomm.data.repository.FirestoreRepository
import com.ludicomm.presentation.theme.RedPlayer
import com.ludicomm.util.CreateMatchInputFields
import com.ludicomm.util.stateHandlers.Resource
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateMatchViewModelTest {

    private val dispatcher: TestDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(dispatcher)

    private lateinit var viewModel: CreateMatchViewModel
    private lateinit var firestoreRepoMock: FirestoreRepository
    private lateinit var authRepoMock: AuthRepository
    private lateinit var bggRepoMock: BGGRepository

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        firestoreRepoMock = mockk()
        authRepoMock = mockk()
        bggRepoMock = mockk()

        val currentUserMock = mockk<FirebaseUser>()
        coEvery { firestoreRepoMock.getUser(any()) } returns User(username = "testUser")
        every { authRepoMock.currentUser() } returns currentUserMock
        every { authRepoMock.currentUser()?.displayName } returns "testUser"

        viewModel = CreateMatchViewModel(firestoreRepoMock, authRepoMock, bggRepoMock)

    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `BGG response returns Resource_Success`() = testScope.runTest {
        val bg1 = BoardGame(name = BoardGame.Name("Roo"), id = "123456")
        val bg2 = BoardGame(name = BoardGame.Name("Root"), id = "234567")
        val bggResultMock = BoardGames(termsOfUseLink = "", boardGames = listOf(bg1, bg2))

        coEvery { bggRepoMock.getBoardGames(any()) } returns
                flow {
                    emit(Resource.Loading())
                    emit(Resource.Success(bggResultMock))
                }

        coEvery { bggRepoMock.getBoardGame(any()) } returns SingleBoardGameList(
            termsOfUseLink = "",
            listOf(SingleBoardGame(id = "123456", thumbnail = "www.test.com/thumb"))
        )

        viewModel.changeInput("Roo", CreateMatchInputFields.GameQuery)

        viewModel.createBGGSuggestions()

        advanceUntilIdle()

        assertThat(viewModel.state.value.isSuccess).isEqualTo("Suggestion list created")

    }

    @Test
    fun `Submit match returns right info`() = testScope.runTest {
        coEvery { firestoreRepoMock.submitMatch(any()) } returns
                flow {
                    emit(Resource.Success(Unit))
                }


        viewModel.setLastGameCLicked("Root")
        viewModel.changeInput("Root", CreateMatchInputFields.GameQuery)

        viewModel.addPlayer(
            PlayerMatchData(
                name = "Player 1",
                faction = "",
                color = RedPlayer.toString(),
                isWinner = false
            )
        )
        viewModel.addPlayer(
            PlayerMatchData(
                name = "Player 2",
                color = Blue.toString(),
                isWinner = true
            )
        )
        viewModel.addPlayer(
            PlayerMatchData(
                name = "Player 3",
                faction = "Black",
                isWinner = false
            )
        )

        viewModel.submitMatch()

        val job = launch {
            viewModel.state.collect { state ->
                assertThat(state.isSuccess).isEqualTo("Match submitted successfully")
            }
        }
        job.cancel()
    }

}