package com.ludicomm.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DrawerState
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ludicomm.R
import com.ludicomm.presentation.components.ImmutableNavigationDrawer
import com.ludicomm.presentation.theme.GreyPlayer
import com.ludicomm.presentation.theme.OrangePlayer
import com.ludicomm.presentation.theme.YellowPlayer
import com.ludicomm.presentation.viewmodel.GameStatsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameStatsScreen(
    viewModel: GameStatsViewModel = hiltViewModel(),
    navController: NavController,
    gameName: String,
    gameUri: String
) {

    val drawerState = DrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val matchList by viewModel.matchList.collectAsState()
    val winnerList by viewModel.winnerList.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.getGame(gameName)
        viewModel.getAllUserGameMatches()
        viewModel.computeWinsPerPlayer()
    }

    Surface {
        ImmutableNavigationDrawer(
            drawerState = drawerState,
            navController = navController,
            signOutFunction = { viewModel.signOut { navController.navigate(LOGIN) } })
        {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = stringResource(R.string.game_stats)) },
                        navigationIcon = {
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(8.dp)
                ) {
                    Text(text = gameName, fontSize = 22.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = stringResource(R.string.games_played, matchList.size))
                                Text(
                                    text = stringResource(
                                        R.string.games_won,
                                        viewModel.computeTotalWins()
                                    )
                                )
                                Text(
                                    text = stringResource(
                                        R.string.average_score,
                                        viewModel.computeAveragePoints()
                                    )
                                )
                                Text(text = stringResource(R.string.best_score) + if (matchList.isNotEmpty()) "${viewModel.getBestScore()}" else "-")
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = stringResource(R.string.best_players), fontSize = 18.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                if (winnerList.isNotEmpty()) {
                                    Row {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            tint = YellowPlayer,
                                            contentDescription = "star icon"
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            text = if (winnerList[0].second == 0) stringResource(R.string.no_player)
                                            else stringResource(
                                                R.string.win,
                                                winnerList[0].first,
                                                winnerList[0].second
                                            ) +
                                                    if (winnerList[0].second > 1) "s)" else ")"
                                        )
                                    }
                                    Row {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            tint = GreyPlayer,
                                            contentDescription = "star icon"
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            text = if (winnerList.size < 2 || winnerList[1].second == 0) stringResource(
                                                R.string.no_player
                                            )
                                            else stringResource(
                                                R.string.win,
                                                winnerList[1].first,
                                                winnerList[1].second
                                            ) +
                                                    if (winnerList[1].second > 1) "s)" else ")"
                                        )
                                    }
                                    Row {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            tint = OrangePlayer,
                                            contentDescription = "star icon"
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            text = if (winnerList.size < 3 || winnerList[2].second == 0) stringResource(
                                                R.string.no_player
                                            )
                                            else stringResource(
                                                R.string.win,
                                                winnerList[2].first,
                                                winnerList[2].second
                                            ) +
                                                    if (winnerList[2].second > 1) "s)" else ")"
                                        )
                                    }
                                }
                            }
                            if (gameUri != "no_image") {
                                AsyncImage(
                                    modifier = Modifier
                                        .scale(2f)
                                        .offset(x = (-28).dp),
                                    model = ImageRequest.Builder(context).data(gameUri)
                                        .build(),
                                    contentDescription = "thumbnail"
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}