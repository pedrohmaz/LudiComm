package com.ludicomm.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.ludicomm.data.repository.AuthRepository
import com.ludicomm.presentation.screens.LOGIN

@Composable
fun AuthGuard (
    authRepository: AuthRepository,
    navHostController: NavHostController,
    content: @Composable () -> Unit
) {
    var isUserLoggedIn by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(key1 = Unit) {
        isUserLoggedIn = authRepository.isUserLoggedIn()
    }

    LaunchedEffect(key1 = isUserLoggedIn) {
        if (isUserLoggedIn == false) {
            navHostController.navigate(LOGIN)
            navHostController.clearBackStack(LOGIN)
        }
    }

    if (isUserLoggedIn == true) content()


}






