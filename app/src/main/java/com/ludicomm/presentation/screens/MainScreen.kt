package com.ludicomm.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ludicomm.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToCreateMatch: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToMyMatches: () -> Unit,
    onNavigateToMyStats: () -> Unit
) {

    val context = LocalContext.current
    val userName by viewModel.username.collectAsState()
    val emailSent by viewModel.emailSent.collectAsState()
    val scope = rememberCoroutineScope()


    Surface {

        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                text = "Welcome $userName!",
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (viewModel.isAccountVerified() == false) {
                Text(
                    text = "Verified account: ${viewModel.isAccountVerified()}"
                )
                Button(onClick = {
                    scope.launch(Dispatchers.IO) {
                        viewModel.sendEmailVerification(onNavigateToLogin).also {
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
                    viewModel.signOut(onNavigateToLogin)
                    onNavigateToLogin()
                }) {
                    Text(text = "Sign Out")
                }
            } else {
                Button(onClick = { onNavigateToCreateMatch() }) {
                    Text(text = "Create match")
                }

                Button(onClick = { onNavigateToMyMatches() }) {
                    Text(text = "My matches")
                }

                Button(onClick = { onNavigateToMyStats() }) {
                    Text(text = "My stats")
                }

                Button(onClick = { viewModel.signOut(onNavigateToLogin) }) {
                    Text(text = "Sign out")
                }
            }
        }

    }
}

