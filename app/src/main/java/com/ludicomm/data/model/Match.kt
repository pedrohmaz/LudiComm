package com.ludicomm.data.model

data class Match(
    val game: String= "",
    val gamePhoto: String? = null,
    val dateAndTime: String = "",
    val thumbnail: String? = null,
    val numberOfPlayers: Int = -1,
    val playerNames: List<String> = emptyList(),
    val playerDataList: MutableList<PlayerMatchData> = mutableListOf(),
    val winners: List<String> = listOf(),
    val obs: String = ""

)


