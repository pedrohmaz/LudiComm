package com.ludicomm.presentation.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ludicomm.data.repository.AuthRepository
import com.ludicomm.presentation.components.AuthGuard
import java.net.URLEncoder

const val LOGIN = "login"
const val SIGNUP = "signup"
const val MAIN = "main"
const val CREATE_MATCH = "create_match"
const val MY_MATCHES = "my_matches"
const val MY_STATS = "my_stats"
const val FRIENDS = "friends"
const val GAME_STATS = "game_stats/{gameName}/{gameUri}"
const val PASSWORD_RETRIEVE = "password_retrieve"


@Composable
fun Navigator(
    navController: NavHostController = rememberNavController(),
    authRepository: AuthRepository
) {

    NavHost(navController = navController, startDestination = LOGIN) {
        composable(LOGIN) {
            LoginScreen(
                onNavigateToSignUp = { navController.navigate(SIGNUP) },
                onNavigateToMain = { navController.navigate(MAIN) },
                onNavigateToPasswordRetrieve = {navController.navigate(PASSWORD_RETRIEVE)}

            )
        }
        composable(SIGNUP) {
            SignUpScreen {
                navController.navigate(LOGIN)
            }
        }
        composable(PASSWORD_RETRIEVE){
            PasswordRetrieveScreen {
                navController.navigate(LOGIN)
            }
        }
        composable(MAIN) {
            AuthGuard(authRepository, navHostController = navController) {
                MainScreen(
                    navController = navController
                )
            }
        }
        composable(CREATE_MATCH) {
            AuthGuard(authRepository = authRepository, navHostController = navController) {
                CreateMatchScreen(
                    navController = navController
                )
            }
        }
        composable(
            MY_MATCHES,
        ) {
            AuthGuard(authRepository = authRepository, navHostController = navController) {
                MyMatchesScreen(
                    navController = navController,
                    onNavigateToGameStats = { gameName, gameUri ->
                        val encodedUri = URLEncoder.encode(gameUri, "UTF-8")
                        navController.navigate("game_stats/$gameName/$encodedUri")
                    }
                )
            }
        }
        composable(MY_STATS) {
            AuthGuard(authRepository = authRepository, navHostController = navController) {
                MyStatsScreen(
                    navController = navController
                )
            }
        }
        composable(FRIENDS) {
            AuthGuard(authRepository = authRepository, navHostController = navController) {
                FriendsScreen(
                    navController = navController
                )
            }
        }
        composable(GAME_STATS,
            arguments = listOf(
                navArgument("gameName") { type = NavType.StringType },
                navArgument("gameUri") { type = NavType.StringType }
                )
        ) { backStackEntry ->
            AuthGuard(authRepository = authRepository, navHostController = navController) {
                val gameName = backStackEntry.arguments?.getString("gameName") ?: ""
                val gameUri: String = backStackEntry.arguments?.getString("gameUri") ?: ""
                GameStatsScreen(
                    navController = navController,
                    gameName = gameName,
                    gameUri = gameUri
                )
            }
        }
    }
}