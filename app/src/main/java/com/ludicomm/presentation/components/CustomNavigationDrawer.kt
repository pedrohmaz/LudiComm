package com.ludicomm.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ludicomm.R
import com.ludicomm.presentation.screens.CREATE_MATCH
import com.ludicomm.presentation.screens.FRIENDS
import com.ludicomm.presentation.screens.MAIN
import com.ludicomm.presentation.screens.MY_MATCHES
import com.ludicomm.presentation.screens.MY_STATS

@Composable
fun MutableNavigationDrawer(
    drawerState: DrawerState,
    onClickMain: () -> Unit,
    onClickCreateMatch: () -> Unit,
    onClickMyMatches: () -> Unit,
    onClickMyStats: () -> Unit,
    onClickSignOut: () -> Unit,
    onClickFriends: () -> Unit,
    content: @Composable () -> Unit
) {

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = MaterialTheme.colorScheme.background) {
                Box(Modifier.fillMaxSize()) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopStart)
                    ) {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = stringResource(id = R.string.app_name),
                            fontSize = 20.sp,
                        )
                        HorizontalDivider()
                        NavigationDrawerItem(
                            label = {
                                Text(
                                    text = stringResource(R.string.main_screen),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            selected = false,
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            shape = RectangleShape,
                            onClick = { onClickMain() }
                        )
                        HorizontalDivider()
                        NavigationDrawerItem(
                            label = {
                                Text(
                                    text = stringResource(id = R.string.create_match),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            selected = false,
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            shape = RectangleShape,
                            onClick = { onClickCreateMatch() }
                        )
                        HorizontalDivider()
                        NavigationDrawerItem(
                            label = {
                                Text(
                                    text = stringResource(id = R.string.my_matches),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            },
                            shape = RectangleShape,
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            selected = false,
                            onClick = { onClickMyMatches() }
                        )
                        HorizontalDivider()
                        NavigationDrawerItem(
                            label = {
                                Text(
                                    text = stringResource(id = R.string.my_stats),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            },
                            shape = RectangleShape,
                            selected = false,
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            onClick = { onClickMyStats() }
                        )
                        HorizontalDivider()
                        NavigationDrawerItem(
                            label = {
                                Text(
                                    text = stringResource(id = R.string.friends),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            },
                            shape = RectangleShape,
                            selected = false,
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            onClick = { onClickFriends() }
                        )
                        HorizontalDivider()
                    }

                    Column(modifier = Modifier.align(Alignment.BottomStart)) {
                        HorizontalDivider()
                        NavigationDrawerItem(
                            label = {
                                Text(
                                    text = stringResource(id = R.string.sign_out),
                                    fontSize = 18.sp
                                )
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            shape = RectangleShape,
                            selected = false,
                            onClick = { onClickSignOut() }
                        )
                    }
                }
            }
        },
        content = content
    )

}

@Composable
fun ImmutableNavigationDrawer(
    drawerState: DrawerState,
    navController: NavController,
    signOutFunction: () -> Unit,
    content: @Composable () -> Unit
) {

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = MaterialTheme.colorScheme.background) {
                Box(Modifier.fillMaxSize()) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopStart)
                    ) {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = stringResource(id = R.string.app_name),
                            fontSize = 20.sp,
                        )
                        HorizontalDivider()
                        NavigationDrawerItem(
                            label = {
                                Text(
                                    text = stringResource(id = R.string.main_screen),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            selected = false,
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            shape = RectangleShape,
                            onClick = { navController.navigate(MAIN) }
                        )
                        HorizontalDivider()
                        NavigationDrawerItem(
                            label = {
                                Text(
                                    text = stringResource(id = R.string.create_match),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            selected = false,
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            shape = RectangleShape,
                            onClick = { navController.navigate(CREATE_MATCH) }
                        )
                        HorizontalDivider()
                        NavigationDrawerItem(
                            label = {
                                Text(
                                    text = stringResource(id = R.string.my_matches),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            },
                            shape = RectangleShape,
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            selected = false,
                            onClick = { navController.navigate(MY_MATCHES) }
                        )
                        HorizontalDivider()
                        NavigationDrawerItem(
                            label = {
                                Text(
                                    text = stringResource(id = R.string.my_stats),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            },
                            shape = RectangleShape,
                            selected = false,
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            onClick = { navController.navigate(MY_STATS) }
                        )
                        HorizontalDivider()
                        NavigationDrawerItem(
                            label = {
                                Text(
                                    text = stringResource(id = R.string.friends),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            },
                            shape = RectangleShape,
                            selected = false,
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            onClick = { navController.navigate(FRIENDS) }
                        )
                        HorizontalDivider()
                    }

                    Column(modifier = Modifier.align(Alignment.BottomStart)) {
                        HorizontalDivider()
                        NavigationDrawerItem(
                            label = {
                                Text(
                                    text = stringResource(id = R.string.sign_out),
                                    fontSize = 18.sp
                                )
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            shape = RectangleShape,
                            selected = false,
                            onClick = { signOutFunction() }
                        )
                    }
                }
            }
        },
        content = content
    )
}