package com.ludicomm.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ludicomm.data.repository.AuthRepository
import com.ludicomm.ui.screens.Navigator
import com.ludicomm.ui.theme.LudiCommTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity:
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

