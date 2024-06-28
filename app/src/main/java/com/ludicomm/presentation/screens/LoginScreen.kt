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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import com.ludicomm.presentation.viewmodel.LoginViewModel
import com.ludicomm.util.LoginInputFields
import com.ludicomm.util.SignUpInputFields
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToSignUp: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    title = {
                        Text("Login")
                    }
                )
            },
        ) { paddingValues ->
            PaddingValues(8.dp)

            val emailText by viewModel.emailInput.collectAsState()
            val passwordText by viewModel.passwordInput.collectAsState()
            val state by viewModel.logInState.collectAsState()
            val scope = rememberCoroutineScope()
            val context = LocalContext.current

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Spacer(modifier = Modifier.height(200.dp))

                Text(text = "Email", fontSize = 24.sp)

                CustomTextField(
                    text = emailText,
                    keyOpt = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    onTextChange = { viewModel.changeInput(LoginInputFields.Email, it) }
                )

                Text(text = "Password", fontSize = 24.sp)

                CustomTextField(
                    text = passwordText,
                    keyOpt = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    onTextChange = { viewModel.changeInput(LoginInputFields.Password, it) }
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
                        viewModel.loginUser(emailText, passwordText)
                    }

                }) {
                    Text(text = "Login")
                }

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    FloatingActionButton(onClick = { onNavigateToSignUp() }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add New User")
                    }
                }

                LaunchedEffect(key1 = state) {
                    if (state.isSuccess?.isNotEmpty() == true) onNavigateToMain()
                    else if (state.isError?.isNotEmpty() == true) {
                        val error = state.isError
                        Toast.makeText(context, "$error", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                LaunchedEffect(key1 = Unit){
                    if (viewModel.isUserLoggedIn()) onNavigateToMain()
                }
            }
        }
    }
}