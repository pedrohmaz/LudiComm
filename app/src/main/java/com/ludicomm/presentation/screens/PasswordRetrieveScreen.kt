package com.ludicomm.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ludicomm.presentation.components.CustomTextField
import com.ludicomm.presentation.viewmodel.PasswordRetrieveViewModel

@Composable
fun PasswordRetrieveScreen(
    viewModel: PasswordRetrieveViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit
) {

    val emailInput by viewModel.emailInput.collectAsState()

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(75.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Type your email and click the 'Send Request' button.")
            Spacer(modifier = Modifier.height(8.dp))
            CustomTextField(
                text = emailInput,
                onTextChange = { viewModel.changeInput(it) },
                label = "Email"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                viewModel.sendPasswordRetrievalEmail()
                onNavigateToLogin()
            }) {
                Text(text = "Send Request")
            }
        }
    }


}