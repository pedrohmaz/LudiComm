package com.ludicomm.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import com.ludicomm.data.model.Match
import com.ludicomm.util.formatDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Composable
fun MyMatchesScreen() {

    val firestore = Firebase.firestore
    val auth = FirebaseAuth.getInstance()
    val matchList = remember { mutableStateListOf<Match>() }

    LaunchedEffect(key1 = Unit) {
        withContext(Dispatchers.IO) {
            val list = firestore.collection("matches")
                .whereArrayContains("playerNames", auth.currentUser?.displayName.toString()).get()
                .await().toObjects<Match>()
            for (item in list) matchList.add(
                Match(
                    game = item.game,
                    dateAndTime = item.dateAndTime,
                    numberOfPlayers = item.numberOfPlayers,
                    playerDataList = item.playerDataList,
                ),
            )
        }
    }

    Surface {
        Column(Modifier.fillMaxSize()) {
            Text(text = "Matches")
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(matchList.toList()) {
                    Text(text = "Game: ${it.game}")
                    Text(text = "Date and time: ${it.dateAndTime?.let { value -> formatDate(value.toLong()) } ?: "no date info"}")
                    it.playerDataList.forEach { player ->
                        Row() {
                            Text(text = "${player.name} - ")
                            Text(text = player.faction, color = Color(player.color.toInt()))
                            Text(text = " = ${player.score}")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

    }


}