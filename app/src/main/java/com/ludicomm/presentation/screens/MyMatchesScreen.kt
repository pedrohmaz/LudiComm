package com.ludicomm.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ludicomm.presentation.components.CustomNavigationDrawer
import com.ludicomm.presentation.viewmodel.MyMatchesViewModel
import com.ludicomm.util.formatDate
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMatchesScreen(
    viewModel: MyMatchesViewModel = hiltViewModel(),
    navRoutes: Map<String, () -> Unit>
) {

    val matchList by viewModel.matchList.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    Surface {
        CustomNavigationDrawer(
            drawerState = drawerState,
            onClickMain = { navRoutes[MAIN]?.invoke() },
            onClickCreateMatch = { navRoutes[CREATE_MATCH]?.invoke() },
            onClickMyMatches = { navRoutes[MY_MATCHES]?.invoke() },
            onClickMyStats = { navRoutes[MY_STATS]?.invoke() },
            onClickSignOut = { navRoutes[LOGIN] }) {
            Scaffold(
                topBar = {
                    TopAppBar(title = { Text(text = "My Matches") }, navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "open/close nav drawer"
                            )
                        }
                    },
                        colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary)
                    )
                }

            ) { innerPadding ->
                if (matchList.isEmpty())
                    Box(Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
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
                                Text(text = match.game, fontSize = 18.sp)
                                Text(
                                    text = match.dateAndTime?.let { value ->
                                        formatDate(
                                            value.toLong()
                                        )
                                    } ?: "no date info")
                                HorizontalDivider(color = Black)
                                Spacer(modifier = Modifier.height(16.dp))
                                match.playerDataList.forEach { player ->
                                    Row {
                                        Text(text = "${player.name} - ")
                                        Text(
                                            text = player.faction,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(player.color.toInt())
                                        )
                                        Text(text = " = ${player.score}")
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Row {
                                    Text(text = "Winner: ")
                                    match.winners.forEach { winner ->
                                        if (winner == match.winners[match.winners.size - 1]) Text(
                                            text = winner, fontSize = 17.sp
                                        )
                                        else Text(
                                            text = "${match.winners[0]}, ",
                                            fontSize = 17.sp
                                        )
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
}