package com.ludicomm.util


import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RegistrationUtilTest {

    @Test
    fun `empty username returns false`() {
        val result = RegistrationUtil.validateRegistration(
            username = "",
            password = "123",
            confirmPassword = "123"
        )
        assertThat(result).isFalse()
    }


    @Test
    fun `valid username and correctly repeated password returns true`() {
        val result = RegistrationUtil.validateRegistration(
            username = "Phmaz",
            password = "Senha123",
            confirmPassword = "Senha123"
        )
        assertThat(result).isTrue()
    }

    @Test
    fun `password repeated incorrectly returns false`() {
        val result = RegistrationUtil.validateRegistration(
            username = "Phmaz",
            password = "Senha123",
            confirmPassword = "Senha124"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `username shorter than 3 digits returns false`() {
        val result = RegistrationUtil.validateRegistration(
            username = "Ph",
            password = "Senha123",
            confirmPassword = "Senha123"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `username longer than 12 digits returns false`() {
        val result = RegistrationUtil.validateRegistration(
            username = "Phmazphmazphmaz",
            password = "Senha123",
            confirmPassword = "Senha123"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `password shortest than 6 digits returns false`() {
        val result = RegistrationUtil.validateRegistration(
            username = "Phmaz",
            password = "Se1",
            confirmPassword = "Se1"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `password longer than 10 digits returns false`() {
        val result = RegistrationUtil.validateRegistration(
            username = "Phmaz",
            password = "Senha123senha123",
            confirmPassword = "Senha123senha123"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `password does not contain uppercase returns false`() {
        val result = RegistrationUtil.validateRegistration(
            username = "Phmaz",
            password = "senha123",
            confirmPassword = "senha123"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `password does not contain numbers returns false`() {
        val result = RegistrationUtil.validateRegistration(
            username = "Phmaz",
            password = "SenhaABC",
            confirmPassword = "SenhaABC"
        )
        assertThat(result).isFalse()
    }




}