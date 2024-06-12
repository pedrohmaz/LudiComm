package com.ludicomm.util

sealed class InputField(){
    data object Username: InputField()
    data object Email: InputField()
    data object Password: InputField()

}
