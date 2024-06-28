package com.ludicomm.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ludicomm.presentation.components.CustomTextField
import com.ludicomm.util.SignUpInputFields
import com.ludicomm.presentation.viewmodel.SignUpViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(viewModel: SignUpViewModel = hiltViewModel(), onNavigateToLogin: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    title = {
                        Text("Create User")
                    }
                )
            },
        ) { paddingValues ->
            PaddingValues(8.dp)

            val usernameInput by viewModel.usernameInput.collectAsState()
            val emailInput by viewModel.emailInput.collectAsState()
            val passwordInput by viewModel.passwordInput.collectAsState()
            val confirmPasswordInput by viewModel.confirmPasswordInput.collectAsState()
            val state by viewModel.signUpState.collectAsState()
            val scope = rememberCoroutineScope()
            val context = LocalContext.current

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Spacer(modifier = Modifier.height(50.dp))

                Text(text = "Username", fontSize = 24.sp)

                CustomTextField(
                    text = usernameInput,
                    keyOpt = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    onTextChange = { viewModel.changeInput(SignUpInputFields.Username, it) }
                )

                Text(text = "Email", fontSize = 24.sp)

                CustomTextField(
                    text = emailInput,
                    keyOpt = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    onTextChange = { viewModel.changeInput(SignUpInputFields.Email, it) }
                )

                Text(text = "Password", fontSize = 24.sp)

                CustomTextField(
                    text = passwordInput,
                    keyOpt = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    onTextChange = { viewModel.changeInput(SignUpInputFields.Password, it) }
                )

                Text(text = "Repeat Password", fontSize = 24.sp)

                CustomTextField(
                    text = confirmPasswordInput,
                    keyOpt = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    onTextChange = { viewModel.changeInput(SignUpInputFields.ConfirmPassword, it) }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator()
                    }
                }

                Button(onClick = {
                    scope.launch {
                        viewModel.registerUser(usernameInput, emailInput, passwordInput)
                    }
                }) {
                    Text(text = "SignUp")
                }


                LaunchedEffect(key1 = state) {
                    if (state.isSuccess?.isNotEmpty() == true) {
                        val success = state.isSuccess
                        Toast.makeText(context, "$success", Toast.LENGTH_SHORT)
                            .show()
                        onNavigateToLogin()
                    }
                    if (state.isError?.isNotEmpty() == true) {
                        val error = state.isError
                        Toast.makeText(context, "$error", Toast.LENGTH_SHORT)
                            .show()
                }


                }

            }
        }
    }
}

