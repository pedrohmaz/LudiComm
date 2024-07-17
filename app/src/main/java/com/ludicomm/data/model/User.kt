package com.ludicomm.data.model

data class User(
    val id: String = "",
    val username: String = "",
    val lowerCaseUsername: String = "",
    val bggName: String = "",
    val friends: List<String> = listOf(),
    val pendingRequestsSent: List<String> = listOf(),
    val pendingRequestsReceived: List<String> = listOf()
)
