package com.ludicomm.util.stateHandlers

data class SignUpState(

    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""

)
