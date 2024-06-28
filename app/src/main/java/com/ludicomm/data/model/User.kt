package com.ludicomm.data.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black

data class User(
    val id: String,
    val username: String,
    val bggName: String = "",
)
