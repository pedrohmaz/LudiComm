package com.ludicomm.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.ludicomm.viewmodel.SecondViewModel
import kotlinx.coroutines.launch

@Composable
fun SecondScreen(viewModel: SecondViewModel = hiltViewModel(), onNavigateToLogin: () -> Unit) {


    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

    }
    Button(onClick = {
        viewModel.signOutUser()
        onNavigateToLogin()
    }) {
        Text(text = "SignOut")
    }

}