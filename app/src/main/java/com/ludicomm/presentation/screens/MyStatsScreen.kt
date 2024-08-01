package com.ludicomm.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.material3.VerticalDivider
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
import androidx.navigation.NavController
import co.yml.charts.common.components.Legends
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.piechart.charts.PieChart
import com.ludicomm.presentation.components.ImmutableNavigationDrawer
import com.ludicomm.presentation.theme.GreyPlayer
import com.ludicomm.presentation.theme.OrangePlayer
import com.ludicomm.presentation.theme.YellowPlayer
import com.ludicomm.presentation.viewmodel.MyStatsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyStatsScreen(
    viewModel: MyStatsViewModel = hiltViewModel(),
    navController: NavController
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val totalMatches by viewModel.totalMatches.collectAsState()
    val totalWins by viewModel.totalWins.collectAsState()
    val gamesMostPlayed by viewModel.gamesMostPlayed.collectAsState()
    val playersMostPlayed by viewModel.playersMostPlayed.collectAsState()


    val pieChartConfig by viewModel.pieChartConfig.collectAsState()
    val winLossChartData by viewModel.winLossChartData.collectAsState()
    val gamesChartData by viewModel.gamesChartData.collectAsState()
    val playersChartData by viewModel.playersChartData.collectAsState()

    Surface {
        ImmutableNavigationDrawer(
            drawerState = drawerState,
            navController = navController,
            signOutFunction = { viewModel.signOut { navController.navigate(LOGIN) }})
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
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(text = "Total matches played: $totalMatches", fontSize = 20.sp)
                        Text(text = "Total matches won: $totalWins", fontSize = 20.sp)

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(155.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {

                            PieChart(
                                modifier = Modifier.size(150.dp),
                                pieChartData = winLossChartData,
                                pieChartConfig = pieChartConfig
                            )
                            VerticalDivider()
                            Legends(
                                legendsConfig = DataUtils.getLegendsConfigFromPieChartData(
                                    winLossChartData,
                                    1
                                )
                            )
                        }

                        HorizontalDivider()

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Game most played:",
                                fontSize = 20.sp
                            )
                            Row {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    tint = YellowPlayer,
                                    contentDescription = "Star Icon"
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = if (gamesMostPlayed[0].second == 0) "No game"
                                    else "${gamesMostPlayed[0].first} (${gamesMostPlayed[0].second} times)"
                                )
                            }
                            Row {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    tint = GreyPlayer,
                                    contentDescription = "Star Icon"
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = if (gamesMostPlayed[1].second == 0) "No game"
                                    else "${gamesMostPlayed[1].first} (${gamesMostPlayed[1].second} times)"
                                )
                            }
                            Row {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    tint = OrangePlayer,
                                    contentDescription = "Star Icon"
                                )

                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = if (gamesMostPlayed[2].second == 0) "No game"
                                    else "${gamesMostPlayed[2].first} (${gamesMostPlayed[2].second} times)"
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .height(155.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                PieChart(
                                    modifier = Modifier.size(150.dp),
                                    pieChartData = gamesChartData,
                                    pieChartConfig = pieChartConfig
                                )
                                VerticalDivider()
                                Legends(
                                    legendsConfig = DataUtils.getLegendsConfigFromPieChartData(
                                        gamesChartData,
                                        2
                                    )
                                )
                            }

                        }

                        HorizontalDivider()

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Players most played with:",
                                fontSize = 20.sp
                            )
                            Row {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    tint = YellowPlayer,
                                    contentDescription = "Star Icon"
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = if (playersMostPlayed[0].second == 0) "No player"
                                    else "${playersMostPlayed[0].first} (${playersMostPlayed[0].second} times)"
                                )
                            }
                            Row {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    tint = GreyPlayer,
                                    contentDescription = "Star Icon"
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = if (playersMostPlayed[1].second == 0) "No player"
                                    else "${playersMostPlayed[1].first} (${playersMostPlayed[1].second} times)"
                                )
                            }
                            Row {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    tint = OrangePlayer,
                                    contentDescription = "Star Icon"
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = if (playersMostPlayed[2].second == 0) "No player"
                                    else "${playersMostPlayed[2].first} (${playersMostPlayed[2].second} times)"
                                )
                            }

                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .height(155.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                PieChart(
                                    modifier = Modifier.size(150.dp),
                                    pieChartData = playersChartData,
                                    pieChartConfig = pieChartConfig
                                )
                                VerticalDivider()
                                Legends(
                                    legendsConfig = DataUtils.getLegendsConfigFromPieChartData(
                                        playersChartData,
                                        2
                                    )
                                )
                            }

                        }

                    }
                }
            }
        }
    }
}