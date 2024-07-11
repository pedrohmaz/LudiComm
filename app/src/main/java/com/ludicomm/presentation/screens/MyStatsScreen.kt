package com.ludicomm.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ludicomm.presentation.components.CustomNavigationDrawer
import com.ludicomm.presentation.theme.GreyPlayer
import com.ludicomm.presentation.theme.OrangePlayer
import com.ludicomm.presentation.theme.YellowPlayer
import com.ludicomm.presentation.viewmodel.MyStatsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyStatsScreen(
    viewModel: MyStatsViewModel = hiltViewModel(),
    navRoutes: Map<String, () -> Unit>
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val totalMatches by viewModel.totalMatches.collectAsState()
    val totalWins by viewModel.totalWins.collectAsState()
    val gamesMostPlayed by viewModel.gamesMostPlayed.collectAsState()
    val playersMostPlayed by viewModel.playersMostPlayed.collectAsState()

    Surface {
        CustomNavigationDrawer(
            drawerState = drawerState,
            onClickMain = { navRoutes[MAIN]?.invoke() },
            onClickCreateMatch = { navRoutes[CREATE_MATCH]?.invoke() },
            onClickMyMatches = { navRoutes[MY_MATCHES]?.invoke() },
            onClickMyStats = { navRoutes[MY_STATS]?.invoke() },
            onClickSignOut = { navRoutes[LOGIN] })
        {
            Scaffold(
                topBar = {
                    TopAppBar(title = { Text(text = "My Stats") }, navigationIcon = {
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
                if (totalMatches == -1)
                    Box(Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Text(text = "Total matches played: $totalMatches", fontSize = 20.sp)
                        Text(text = "Total matches won: $totalWins", fontSize = 20.sp)

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)){
                            Text(
                                text = "Game most played:",
                                fontSize = 20.sp
                            )
                            Row(){
                                Icon(imageVector = Icons.Default.Star, tint = YellowPlayer, contentDescription = "Star Icon")
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(text = "${gamesMostPlayed[0].first} (${gamesMostPlayed[0].second} times)")
                            }
                            Row{
                                Icon(imageVector = Icons.Default.Star, tint = GreyPlayer, contentDescription = "Star Icon")
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(text = "${gamesMostPlayed[1].first} (${gamesMostPlayed[1].second} times)")
                            }
                            Row{
                                Icon(imageVector = Icons.Default.Star, tint = OrangePlayer, contentDescription = "Star Icon")
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(text = "${gamesMostPlayed[2].first} (${gamesMostPlayed[2].second} times)")
                            }
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)){
                            Text(
                                text = "Players most played with:",
                                fontSize = 20.sp
                            )
                            Row(){
                                Icon(imageVector = Icons.Default.Star, tint = YellowPlayer, contentDescription = "Star Icon")
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(text = "${playersMostPlayed[0].first} (${playersMostPlayed[0].second} times)")
                            }
                            Row{
                                Icon(imageVector = Icons.Default.Star, tint = GreyPlayer, contentDescription = "Star Icon")
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(text = "${playersMostPlayed[1].first} (${playersMostPlayed[1].second} times)")
                            }
                            Row{
                                Icon(imageVector = Icons.Default.Star, tint = OrangePlayer, contentDescription = "Star Icon")
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(text = "${playersMostPlayed[2].first} (${playersMostPlayed[2].second} times)")
                            }
                        }

                    }
                }
            }
        }
    }
}