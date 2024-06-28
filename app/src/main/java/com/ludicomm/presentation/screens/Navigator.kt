package com.ludicomm.presentation.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ludicomm.data.repository.AuthRepository
import com.ludicomm.presentation.components.AuthGuard

const val LOGIN = "login"
const val SIGNUP = "signup"
const val MAIN = "main"
const val CREATE_MATCH = "create_match"
const val MY_MATCHES = "my_matches"

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
                    onNavigateToCreateMatch = { navController.navigate(CREATE_MATCH) },
                    onNavigateToLogin = { navController.navigate(LOGIN) },
                    onNavigateToMyMatches = {navController.navigate(MY_MATCHES)}
                )
            }
        }
        composable(CREATE_MATCH){
            AuthGuard(authRepository = authRepository, navHostController = navController) {
                CreateMatchScreen {
                    navController.navigate(MAIN)
                }
            }
        }
        composable(MY_MATCHES){
            AuthGuard(authRepository = authRepository, navHostController = navController) {
                MyMatchesScreen()
            }
        }
    }
}