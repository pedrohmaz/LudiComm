package com.ludicomm.presentation.viewmodel

import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseUser
import com.ludicomm.data.model.Match
import com.ludicomm.data.repository.AuthRepository
import com.ludicomm.data.repository.FirestoreRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class MyMatchesViewModelTest() {

    companion object {
        private const val USER = "testUser"
    }

    private lateinit var firestoreRepoMock: FirestoreRepository
    private lateinit var authRepoMock: AuthRepository

    private val dispatcher: TestDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(dispatcher)

    private val filteredList = listOf(
        Match(playerNames = listOf(USER, "test1", "test2")),
        Match(playerNames = listOf("test1", USER))
    )

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        firestoreRepoMock = mockk()
        authRepoMock = mockk()
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `getAllUserMatches in init block success returns correct match list`() = testScope.runTest {
        every { authRepoMock.currentUser() } returns mockk<FirebaseUser> {
            every { this@mockk.displayName } returns USER
        }
        coEvery { firestoreRepoMock.getAllUserMatches(USER) } returns filteredList

        val viewModel = MyMatchesViewModel(firestoreRepoMock, authRepoMock)

        advanceUntilIdle()

        assertThat(viewModel.matchList.value).isEqualTo(filteredList)
    }


    @Test
    fun `getAllUserMatches in init block fail returns empty match list`() = testScope.runTest {
        every { authRepoMock.currentUser() } returns mockk<FirebaseUser> {
            every { this@mockk.displayName } returns USER
        }
        coEvery { firestoreRepoMock.getAllUserMatches(USER) } returns emptyList()

        val viewModel = MyMatchesViewModel(firestoreRepoMock, authRepoMock)

        advanceUntilIdle()

        assertThat(viewModel.matchList.value).isEqualTo(emptyList<Match>())
    }


}