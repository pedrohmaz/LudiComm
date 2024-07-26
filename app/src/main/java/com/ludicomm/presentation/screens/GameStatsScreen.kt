package com.ludicomm.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ludicomm.presentation.components.CustomNavigationDrawer
import com.ludicomm.presentation.viewmodel.GameStatsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameStatsScreen(
    viewModel: GameStatsViewModel = hiltViewModel(),
    navRoutes: Map<String, () -> Unit>,
    gameName: String,
    gameUri: String
) {

    val drawerState = DrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val matchList by viewModel.matchList.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.getGame(gameName)
        viewModel.getAllUserGameMatches()
    }

    Surface {
        CustomNavigationDrawer(
            drawerState = drawerState,
            onClickMain = { navRoutes[MAIN]?.invoke() },
            onClickCreateMatch = { navRoutes[CREATE_MATCH]?.invoke() },
            onClickMyMatches = { navRoutes[MY_MATCHES]?.invoke() },
            onClickMyStats = { navRoutes[MY_STATS]?.invoke() },
            onClickSignOut = { viewModel.signOut { navRoutes[LOGIN]?.invoke() } },
            onClickFriends = { navRoutes[FRIENDS]?.invoke() })
        {
            Scaffold(
                topBar = {
                    TopAppBar(title = { Text(text = "Game Stats") }, navigationIcon = {
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
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(8.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = gameName, fontSize = 22.sp)
                        if (gameUri != "no_image") {
                            AsyncImage(
                                modifier = Modifier.scale(2f).offset(x = (-28).dp, y = 16.dp),
                                model = ImageRequest.Builder(context).data(gameUri).build(),
                                contentDescription = "thumbnail"
                            )
                        }
                    }
                    Text(text = "Games Played: ${matchList.size}", fontSize = 18.sp)
                    Text(text = "Games Won: ${viewModel.computeTotalWins()}", fontSize = 18.sp)
                }
            }
        }
    }


}