package com.ludicomm.presentation.viewmodel

import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.AuthResult
import com.ludicomm.data.repository.AuthRepository
import com.ludicomm.presentation.viewmodel.LoginViewModel
import com.ludicomm.util.stateHandlers.Resource
import io.mockk.coEvery
import io.mockk.mockk
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
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    companion object {
        private const val EMAIL = "test@testmail.com"
        private const val PASSWORD = "Test1234"
        private const val AUTH_ERROR_MESSAGE = "Login Failed"
    }

    private val dispatcher = StandardTestDispatcher()
    private val testScope = TestScope(dispatcher)

    private lateinit var authRep: AuthRepository
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        authRep = mockk<AuthRepository>()
        viewModel = LoginViewModel(authRep)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `auth successful result returns success message`() = testScope.runTest {
        val authResultMock = mockk<AuthResult>()
        coEvery { authRep.loginUser(EMAIL, PASSWORD) } returns flowOf(
            Resource.Success(
                authResultMock
            )
        )

        viewModel.loginUser(EMAIL, PASSWORD).join()
        assertThat(viewModel.logInState.value.isSuccess).isEqualTo("Login Success")

    }

    @Test
    fun `auth fail result returns error message`() = testScope.runTest {
        coEvery { authRep.loginUser(EMAIL, PASSWORD) } returns flowOf(
            Resource.Error(
                AUTH_ERROR_MESSAGE
            )
        )
        viewModel.loginUser(EMAIL, PASSWORD).join()
        assertThat(viewModel.logInState.value.isError).isEqualTo(AUTH_ERROR_MESSAGE)

    }

    @Test
    fun `auth loading result returns true`() = testScope.runTest {
        coEvery { authRep.loginUser(EMAIL, PASSWORD) } returns flowOf(
            Resource.Loading()
        )
        viewModel.loginUser(EMAIL, PASSWORD).join()

        assertThat(viewModel.logInState.value.isLoading).isTrue()

    }

}