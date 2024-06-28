package com.ludicomm.util

object RegistrationUtil {
    

    fun validateRegistration(username: String, password: String, confirmPassword: String) : Boolean {
        if(username.isEmpty()) return false
        if(!password.contains("[A-Z]".toRegex())) return false
        if (!password.contains("[0-9]".toRegex())) return false
        if(password != confirmPassword) return false
        if (username.length !in 3..12 || password.length !in 6..10) return false
        // todo if(!validUserName) return false
        return true
    }

}