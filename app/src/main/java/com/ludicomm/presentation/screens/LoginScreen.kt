package com.ludicomm.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ludicomm.R
import com.ludicomm.presentation.components.CustomTextField
import com.ludicomm.presentation.viewmodel.LoginViewModel
import com.ludicomm.util.LoginInputFields
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToSignUp: () -> Unit,
    onNavigateToMain: () -> Unit,
    onNavigateToPasswordRetrieve: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
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

            val emailInput by viewModel.emailInput.collectAsState()
            val passwordInput by viewModel.passwordInput.collectAsState()
            val state by viewModel.logInState.collectAsState()
            val scope = rememberCoroutineScope()
            val context = LocalContext.current

            var showPassword by remember {
                mutableStateOf(false)
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Spacer(modifier = Modifier.height(200.dp))

                    Text(text = stringResource(R.string.email), fontSize = 24.sp)

                    CustomTextField(
                        text = emailInput,
                        keyOpt = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        onTextChange = { viewModel.changeInput(LoginInputFields.Email, it) }
                    )

                    Text(text = stringResource(R.string.password), fontSize = 24.sp)

                    OutlinedTextField(
                        value = passwordInput,
                        visualTransformation = if (!showPassword) PasswordVisualTransformation()
                        else VisualTransformation.None,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        onValueChange = { viewModel.changeInput(LoginInputFields.Password, it) }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator()
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = showPassword, onCheckedChange = {
                            showPassword = !showPassword
                        })
                        Text(text = stringResource(R.string.show_password))
                    }

                    Button(onClick = {
                        scope.launch {
                            viewModel.loginUser(emailInput, passwordInput)
                        }

                    }) {
                        Text(text = stringResource(R.string.login))
                    }
                    Spacer(Modifier.height(50.dp))
                    TextButton(
                        modifier = Modifier.padding(8.dp),
                        onClick = { onNavigateToPasswordRetrieve() }) {
                        Text(text = stringResource(R.string.i_forgot_my_password))
                    }
                }

                FloatingActionButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.BottomEnd),
                    onClick = { onNavigateToSignUp() }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_new_user)
                    )
                }
            }


            LaunchedEffect(key1 = state) {
                if (state.isSuccess?.isNotEmpty() == true) onNavigateToMain()
                else if (state.isError?.isNotEmpty() == true) {
                    val error = state.isError
                    Toast.makeText(context, error, Toast.LENGTH_SHORT)
                        .show()
                }
            }
            LaunchedEffect(key1 = Unit) {
                if (viewModel.isUserLoggedIn()) onNavigateToMain()
            }
        }
    }
}
