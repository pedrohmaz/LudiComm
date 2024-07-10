package com.ludicomm.presentation.components

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomNavigationDrawer(drawerState: DrawerState, onClick1: () -> Unit, onClick2: () -> Unit, onClick3: () -> Unit, content:@Composable () -> Unit) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = MaterialTheme.colorScheme.primaryContainer) {
                Text(modifier = Modifier.padding(16.dp), text = "Navigate", fontSize = 22.sp)
                HorizontalDivider()
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "Create Match",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    selected = false,
                    colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = MaterialTheme.colorScheme.secondaryContainer),
                    onClick = {onClick1()}
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "My Matches",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    },
                    colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = MaterialTheme.colorScheme.secondaryContainer),
                    selected = false,
                    onClick = { onClick2() }
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "My Stats",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    },
                    selected = false,
                    colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = MaterialTheme.colorScheme.secondaryContainer),
                    onClick = { onClick3() }
                )
            }
        },
        content = content
    )

}