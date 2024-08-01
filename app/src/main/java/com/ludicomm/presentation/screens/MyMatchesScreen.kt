package com.ludicomm.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ludicomm.presentation.components.ImmutableNavigationDrawer
import com.ludicomm.presentation.viewmodel.MyMatchesViewModel
import com.ludicomm.util.formatDate
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMatchesScreen(
    viewModel: MyMatchesViewModel = hiltViewModel(),
    navController: NavController,
    onNavigateToGameStats: (gameName: String, gameUri: String) -> Unit
) {

    val matchList by viewModel.matchList.collectAsState()
    val noMatches by viewModel.noMatches.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val toggleConfirmDeleteDialog by viewModel.toggleConfirmDeleteDialog.collectAsState()

    Surface {
        ImmutableNavigationDrawer(
            drawerState = drawerState,
            navController = navController,
            signOutFunction = { viewModel.signOut { navController.navigate(LOGIN) } })
        {
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

                if (toggleConfirmDeleteDialog.first) {
                    AlertDialog(
                        title = { Text(text = "Delete Match") },
                        text = { Text(text = "Are you sure you want to delete this match?") },
                        onDismissRequest = { viewModel.toggleConfirmDeleteDialog(false, "") },
                        confirmButton = {
                            TextButton(onClick = {
                                viewModel.deleteMatch(toggleConfirmDeleteDialog.second)
                                viewModel.toggleConfirmDeleteDialog(false, "")
                            }) {
                                Text(text = "Confirm")
                            }
                        }, dismissButton = {
                            TextButton(onClick = {
                                viewModel.toggleConfirmDeleteDialog(
                                    false,
                                    ""
                                )
                            }) {
                                Text(text = "Dismiss")
                            }
                        }
                    )
                }

                if (noMatches) Box(Modifier.fillMaxSize()) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center),
                        text = "No matches yet :(",
                        fontSize = 22.sp
                    )
                }
                else if (matchList.isEmpty())
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
                                // TextButton(onClick = { onNavigateToGameStats(match.game, match.thumbnail ?: "no_image") }) {
                                Text(modifier = Modifier.clickable {
                                    onNavigateToGameStats(
                                        match.game,
                                        match.thumbnail ?: "no_image"
                                    )
                                }, text = match.game, fontSize = 18.sp)
                                //   }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = formatDate(
                                            match.dateAndTime.toLong()
                                        )
                                    )
                                    IconButton(onClick = {
                                        viewModel.toggleConfirmDeleteDialog(
                                            true,
                                            match.dateAndTime
                                        )
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete match"
                                        )
                                    }
                                }
                                HorizontalDivider(color = Black)
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        match.playerDataList.forEach { player ->
                                            Row {
                                                Text(text = "${player.name} - ")
                                                Text(
                                                    text = player.faction,
                                                    fontWeight = Bold,
                                                    color = Color(player.color.toInt())
                                                )
                                                Text(text = " = ${player.score}")
                                            }
                                        }
                                    }
                                    AsyncImage(
                                        modifier = Modifier
                                            .scale(1.5f)
                                            .padding(horizontal = 16.dp, vertical = 16.dp),
                                        model = ImageRequest.Builder(context).data(match.thumbnail)
                                            .build(),
                                        contentDescription = "thumbnail"
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row {
                                    Text(text = "Winner: ")
                                    match.winners.forEach { winner ->
                                        if (winner == match.winners[match.winners.size - 1]) Text(
                                            text = winner,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        else Text(
                                            text = "${match.winners[0]}, ",
                                            fontSize = 17.sp, fontWeight = FontWeight.SemiBold
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