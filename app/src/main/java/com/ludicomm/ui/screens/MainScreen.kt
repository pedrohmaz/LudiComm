package com.ludicomm.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.ludicomm.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToSecond: () -> Unit,
    onNavigateToLogin: () -> Unit
) {

    val context = LocalContext.current
    val userName by viewModel.username.collectAsState()
    val emailSent by viewModel.emailSent.collectAsState()
    val scope = rememberCoroutineScope()


    Surface {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome $userName!"
            )
            if (viewModel.isAccountVerified() == false) {
                Text(
                    text = "Verified account: ${viewModel.isAccountVerified()}"
                )
                Button(onClick = {
                    scope.launch(Dispatchers.IO) {
                        viewModel.sendEmailVerification().also {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                            }
                        }
                        withContext(Dispatchers.Main) { if (emailSent) onNavigateToLogin() }
                    }
                }) {
                    Text(text = "Verify email")
                }
                Button(onClick = {
                    viewModel.signOut()
                    onNavigateToLogin()
                }) {
                    Text(text = "Sign Out")
                }
            } else {
                Button(onClick = { onNavigateToSecond() }) {
                    Text(text = "Go to second")
                }
            }
        }

    }
}

