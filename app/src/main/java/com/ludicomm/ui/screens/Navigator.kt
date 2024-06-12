package com.ludicomm.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ludicomm.data.repository.AuthRepository
import com.ludicomm.ui.components.AuthGuard

const val LOGIN = "login"
const val SIGNUP = "signup"
const val MAIN = "main"
const val SECOND = "second"

@Composable
fun Navigator(navController: NavHostController = rememberNavController(), authRepository: AuthRepository) {

    NavHost(navController = navController, startDestination = LOGIN) {
        composable(LOGIN) {
            LoginScreen(
                onNavigateToSignUp = { navController.navigate(SIGNUP) },
                onNavigateToMain = { navController.navigate(MAIN) }
            )
        }
        composable(SIGNUP) {
            SignUpScreen {
                navController.navigate(LOGIN)
            }
        }
        composable(MAIN) {
            AuthGuard(authRepository, navHostController = navController) {
                MainScreen(
                    onNavigateToSecond = { navController.navigate(SECOND) },
                    onNavigateToLogin = { navController.navigate(LOGIN) }
                )
            }
        }
        composable(SECOND){
            AuthGuard(authRepository = authRepository, navHostController = navController) {
                SecondScreen{
                    navController.navigate(LOGIN)
                }
            }
        }
    }
}