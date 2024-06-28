package com.ludicomm.presentation

import android.app.Application
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ludicomm.data.repository.AuthRepository
import com.ludicomm.presentation.screens.Navigator
import com.ludicomm.presentation.theme.LudiCommTheme
import com.ludicomm.util.ConnectivityObserver
import com.ludicomm.util.ConnectivityObserverImpl
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity :
    ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LudiCommTheme {
                Navigator(authRepository = authRepository)
            }
        }
    }

}

