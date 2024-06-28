package com.ludicomm.util

sealed class SignUpInputFields {
    data object Username: SignUpInputFields()
    data object Email: SignUpInputFields()
    data object Password: SignUpInputFields()
    data object ConfirmPassword: SignUpInputFields()

}

sealed class LoginInputFields {
    data object Email: LoginInputFields()
    data object Password: LoginInputFields()
}

sealed class CreateMatchInputFields {
    data object GameQuery: CreateMatchInputFields()
    data object Name: CreateMatchInputFields()
    data object ColorOrFaction: CreateMatchInputFields()
    data object Score: CreateMatchInputFields()
}
