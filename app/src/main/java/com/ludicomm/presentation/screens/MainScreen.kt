package com.ludicomm.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ludicomm.presentation.components.CustomNavigationDrawer
import com.ludicomm.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    navRoutes: Map<String, () -> Unit>
) {

    val context = LocalContext.current
    val userName by viewModel.username.collectAsState()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


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
                    TopAppBar(title = { Text(text = "Ludicomm") }, navigationIcon = {
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
                Box (
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())){
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(innerPadding),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(100.dp))
                        Text(
                            text = "Welcome $userName!",
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        if (viewModel.isAccountVerified() == false) {
                            Text(
                                text = "Verified account: ${viewModel.isAccountVerified()}"
                            )
                            Button(onClick = {
                                scope.launch {
                                    viewModel.sendEmailVerification { navRoutes[LOGIN]?.invoke() }
                                        .also {
                                            withContext(Dispatchers.Main) {
                                                Toast.makeText(context, it, Toast.LENGTH_SHORT)
                                                    .show()
                                            }
                                        }
                                }
                            }) {
                                Text(text = "Verify email")
                            }
                            Button(onClick = {
                                viewModel.signOut { navRoutes[LOGIN]?.invoke() }
                                navRoutes[LOGIN]?.invoke()
                            }) {
                                Text(text = "Sign Out")
                            }
                        } else {
                            Button(onClick = { navRoutes[CREATE_MATCH]?.invoke() }) {
                                Text(text = "Create match")
                            }

                            Button(onClick = { navRoutes[MY_MATCHES]?.invoke() }) {
                                Text(text = "My matches")
                            }

                            Button(onClick = { navRoutes[MY_STATS]?.invoke() }) {
                                Text(text = "My stats")
                            }

                            Button(onClick = { navRoutes[FRIENDS]?.invoke() }) {
                                Text(text = "Friends")
                            }

                        }

                    }
                    Button(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(vertical = 16.dp),
                        onClick = { viewModel.signOut { navRoutes[LOGIN]?.invoke() } }) {
                        Text(text = "Sign out")
                    }
                }

            }
        }
    }
}




