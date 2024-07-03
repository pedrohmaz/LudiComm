package com.ludicomm.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ludicomm.presentation.viewmodel.MyMatchesViewModel
import com.ludicomm.util.formatDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMatchesScreen(viewModel: MyMatchesViewModel = hiltViewModel()) {

    val matchList by viewModel.matchList.collectAsState()

    Surface {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "My matches") },
                    colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary)
                )
            }
        ) { innerPadding ->
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(8.dp)
            ) {
                items(matchList.toList()) { match ->
                    Card(
                        modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ), border = BorderStroke(1.dp, Black)
                    ) {
                        Column(Modifier.padding(8.dp)) {
                            Text(text = "Game: ${match.game}")
                            Text(
                                text = "Date and time: ${
                                    match.dateAndTime?.let { value ->
                                        formatDate(
                                            value.toLong()
                                        )
                                    } ?: "no date info"
                                }")
                            Spacer(modifier = Modifier.height(16.dp))
                            match.playerDataList.forEach { player ->
                                Row {
                                    Text(text = "${player.name} - ")
                                    Text(text = player.faction, color = Color(player.color.toInt()))
                                    Text(text = " = ${player.score}")
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row {
                                Text(text = "Winner: ")
                                match.winners.forEach { winner ->
                                    if (winner == match.winners[match.winners.size - 1]) Text(text = winner)
                                    else Text(text = "${match.winners[0]}, ")
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }


        }
    }


}