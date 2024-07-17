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
const val MY_STATS = "my_stats"
const val FRIENDS = "friends"


@Composable
fun Navigator(navController: NavHostController = rememberNavController(), authRepository: AuthRepository) {

    val navRoutes = mapOf(
        LOGIN to {navController.navigate(LOGIN)},
        SIGNUP to {navController.navigate(SIGNUP)},
        MAIN to {navController.navigate(MAIN)},
        CREATE_MATCH to {navController.navigate(CREATE_MATCH)},
        MY_MATCHES to {navController.navigate(MY_MATCHES)},
        MY_STATS to {navController.navigate(MY_STATS)},
        FRIENDS to {navController.navigate(FRIENDS)}
        )

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
                 navRoutes = navRoutes
                )
            }
        }
        composable(CREATE_MATCH){
            AuthGuard(authRepository = authRepository, navHostController = navController) {
                CreateMatchScreen (
                    navRoutes = navRoutes
                )
            }
        }
        composable(MY_MATCHES){
            AuthGuard(authRepository = authRepository, navHostController = navController) {
                MyMatchesScreen(
                    navRoutes = navRoutes
                )
            }
        }
        composable(MY_STATS){
            AuthGuard(authRepository = authRepository, navHostController = navController) {
                MyStatsScreen(
                    navRoutes = navRoutes
                )
            }
        }
        composable(FRIENDS){
            AuthGuard(authRepository = authRepository, navHostController = navController) {
                FriendsScreen(
                    navRoutes = navRoutes
                )
            }
        }
    }
}