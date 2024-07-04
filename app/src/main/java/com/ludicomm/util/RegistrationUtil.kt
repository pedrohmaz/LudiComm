package com.ludicomm.util

import com.ludicomm.data.model.PlayerMatchData
import com.ludicomm.util.stateHandlers.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object RegistrationUtil {

    private const val USERNAME_RULES_MESSAGE = "Your username must contain 3-12 characters"
    private const val BLANK_EMAIL_MESSAGE = "Please type your email account"
    private const val PASSWORD_RULES_MESSAGE =
        "Your password must contain 6-10 characters, at least one upper case and one digit."
    private const val CONFIRM_PASSWORD_MESSAGE =
        "Make sure the two password fields have the same password"


    suspend fun validateUserRegistration(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Flow<Resource<Unit>> {
        var success = true
        return flow {
            if (username.isEmpty()) {
                success = false
                emit(Resource.Error(USERNAME_RULES_MESSAGE))
            } else if (username.length !in 3..12) {
                success = false
                emit(Resource.Error(USERNAME_RULES_MESSAGE))
            } else if (email.isBlank()) {
                success = false
                emit(Resource.Error(BLANK_EMAIL_MESSAGE))
            } else if (!password.contains("[A-Z]".toRegex())) {
                success = false
                emit(Resource.Error(PASSWORD_RULES_MESSAGE))
            } else if (password.length !in 6..10) {
                success = false
                emit(Resource.Error(PASSWORD_RULES_MESSAGE))
            } else if (!password.contains("[0-9]".toRegex())) {
                success = false
                emit(Resource.Error(PASSWORD_RULES_MESSAGE))
            } else if (password != confirmPassword) {
                success = false
                emit(Resource.Error(CONFIRM_PASSWORD_MESSAGE))
            }
            // todo if(!validUserName) return false
            if (success) emit(Resource.Success(Unit))
        }
    }


    private const val BLANK_GAME_MESSAGE = "Please select a game"
    private const val INVALID_GAME_MESSAGE = "Please select a valid game"
    private const val NO_PLAYERS_MESSAGE = "Your match must contain at least one player"

    fun validateMatchSubmission(
        game: String,
        clickedGame: String,
        inputtedGame: String,
        playerList: MutableList<PlayerMatchData>
    ): Resource<Unit> {
        if (game.isBlank()) return Resource.Error(BLANK_GAME_MESSAGE)
        else if (clickedGame != inputtedGame) return Resource.Error(INVALID_GAME_MESSAGE)
        else if (playerList.isEmpty()) return Resource.Error(NO_PLAYERS_MESSAGE)
        return Resource.Success(Unit)
    }

}