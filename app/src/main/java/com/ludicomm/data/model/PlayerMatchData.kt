package com.ludicomm.data.model

import androidx.compose.ui.graphics.Color.Companion.Black

data class PlayerMatchData(
    val name:String = "null name",
    val faction: String = "",
    val score: String = "0",
    val color: String = Black.toString(),

    )


