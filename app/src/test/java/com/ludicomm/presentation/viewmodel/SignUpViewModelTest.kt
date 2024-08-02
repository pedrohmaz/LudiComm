package com.ludicomm.presentation.viewmodel

import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.AuthResult
import com.ludicomm.data.repository.AuthRepository
import com.ludicomm.data.repository.FirestoreRepository
import com.ludicomm.presentation.viewmodel.SignUpViewModel
import com.ludicomm.util.SignUpInputFields
import com.ludicomm.util.RegistrationUtil
import com.ludicomm.util.stateHandlers.Resource
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignUpViewModelTest {

    companion object {
        private const val VALID_USERNAME = "Bob123"
        private const val EMAIL = "test@testmail.com"
        private const val VALID_PASSWORD = "Test1234"
        private const val INVALID_PASSWORD = "bobabc"
        private const val UID = "nid8n7yewd3n819c7ybn78"
        private const val AUTH_ERROR_MESSAGE = "Could not authenticate"
        private const val FIRESTORE_ERROR_MESSAGE = "Firestore Error"
        private const val PASSWORD_RULES_MESSAGE =
            "Your password must contain 6-10 characters, at least one upper case and one digit."
    }

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var authRep: AuthRepository
    private lateinit var firestoreRep: FirestoreRepository
    private lateinit var viewModel: SignUpViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        authRep = mockk()
        firestoreRep = mockk()
        mockkObject(RegistrationUtil)
        viewModel = SignUpViewModel(authRep, firestoreRep)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `registerUser success returns success message`() = testScope.runTest {
        val authResultMock = mockk<AuthResult>()
        val firestoreResultMock = Resource.Success(Unit)

        coEvery { authRep.registerUser(VALID_USERNAME, EMAIL, VALID_PASSWORD) } returns flowOf(
            Resource.Success(
                authResultMock
            )
        )
        coEvery { firestoreRep.registerUser(UID, VALID_USERNAME, EMAIL) } returns flowOf(
            firestoreResultMock
        )
        coEvery { RegistrationUtil.validateUserRegistration(any(), any(), any(), any()) } returns flowOf(Resource.Success(Unit))
        every { authRep.currentUser() } returns mockk(relaxed = true) {
            every { this@mockk.uid } returns UID
        }

        viewModel.registerUser(VALID_USERNAME, EMAIL, VALID_PASSWORD).join()
        assertThat(viewModel.signUpState.value.isSuccess).isEqualTo("User Created")

    }

    @Test
    fun `validateRegistration fail returns error message `() = testScope.runTest {
        viewModel.changeInput(SignUpInputFields.ConfirmPassword, INVALID_PASSWORD)
        viewModel.registerUser(VALID_USERNAME, EMAIL, INVALID_PASSWORD).join()
        assertThat(viewModel.signUpState.value.isError).isEqualTo(PASSWORD_RULES_MESSAGE)
    }

    @Test
    fun `authRep_registerUser fail returns error message`() = testScope.runTest {
        coEvery { authRep.registerUser(VALID_USERNAME, EMAIL, VALID_PASSWORD) } returns flowOf(
            Resource.Error(AUTH_ERROR_MESSAGE)
        )
        viewModel.changeInput(SignUpInputFields.ConfirmPassword, VALID_PASSWORD)
        viewModel.registerUser(VALID_USERNAME, EMAIL, VALID_PASSWORD).join()
        assertThat(viewModel.signUpState.value.isError).isEqualTo(AUTH_ERROR_MESSAGE)

    }

    @Test
    fun `firestoreRep fail returns error message`() = testScope.runTest {
        val authResultMock = mockk<AuthResult>()

        coEvery { authRep.registerUser(VALID_USERNAME, EMAIL, VALID_PASSWORD) } returns flowOf(
            Resource.Success(
                authResultMock
            )
        )
        every { authRep.currentUser() } returns mockk(relaxed = true) {
            every { this@mockk.uid } returns UID
        }
        coEvery {
            firestoreRep.registerUser(
                UID,
                VALID_USERNAME,
                EMAIL
            )
        } returns flowOf(Resource.Error(FIRESTORE_ERROR_MESSAGE))
        viewModel.changeInput(SignUpInputFields.ConfirmPassword, VALID_PASSWORD)
        viewModel.registerUser(VALID_USERNAME, EMAIL, VALID_PASSWORD).join()
        assertThat(viewModel.signUpState.value.isError).isEqualTo("Could not register user: $FIRESTORE_ERROR_MESSAGE")

    }


}


