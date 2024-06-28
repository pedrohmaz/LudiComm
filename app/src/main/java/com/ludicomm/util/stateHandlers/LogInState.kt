package com.ludicomm.util.stateHandlers

data class LogInState(

    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""

)