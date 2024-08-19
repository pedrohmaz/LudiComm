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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ludicomm.R
import com.ludicomm.presentation.components.CustomTextField
import com.ludicomm.presentation.components.ImmutableNavigationDrawer
import com.ludicomm.presentation.components.MainSquareButton
import com.ludicomm.presentation.theme.LightBluePlayer
import com.ludicomm.presentation.theme.Purple40
import com.ludicomm.presentation.theme.PurplePlayer
import com.ludicomm.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavController
) {

    val context = LocalContext.current
    val userName by viewModel.username.collectAsState()
    val scope = rememberCoroutineScope()
    val state by viewModel.state.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val isPasswordConfirmationNeeded by viewModel.isPasswordConfirmationNeeded.collectAsState()
    val newPasswordInput by viewModel.newPasswordInput.collectAsState()
    val confirmNewPasswordInput by viewModel.confirmNewPasswordInput.collectAsState()


    LaunchedEffect(key1 = state) {
        if (state.isSuccess.isNotEmpty()) {
            Toast.makeText(context, state.isSuccess, Toast.LENGTH_SHORT).show()
        }
        if (state.isError.isNotEmpty()) {
            Toast.makeText(context, state.isError, Toast.LENGTH_SHORT).show()
            viewModel.resetState()
        }
    }

    Surface {
        if (viewModel.isAccountVerified() == false) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(70.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Verified account: ${viewModel.isAccountVerified()}"
                )
                Button(onClick = {
                    scope.launch {
                        viewModel.sendEmailVerification { navController.navigate(LOGIN) }
                            .also {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, it, Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                    }
                }) {
                    Text(text = stringResource(R.string.verify_email))
                }
                Button(onClick = {
                    viewModel.signOut { navController.navigate(LOGIN) }
                    // navController.navigate(LOGIN)    //not sure if needed
                }) {
                    Text(text = stringResource(R.string.sign_out))
                }
            }
        } else if (isPasswordConfirmationNeeded) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(50.dp)
            ) {
                Text(text = stringResource(R.string.please_confirm_your_new_password))
                CustomTextField(
                    text = newPasswordInput,
                    onTextChange = { viewModel.changeNewPasswordInput(it) },
                    label = stringResource(R.string.new_password)
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextField(
                    text = confirmNewPasswordInput,
                    onTextChange = { viewModel.changeConfirmNewPasswordInput(it) },
                    label = stringResource(R.string.confirm_new_password)
                )
                Button(onClick = {
                    viewModel.submitNewPassword()
                }) {
                    Text(text = stringResource(R.string.submit))
                }
            }
        } else if (state.isLoading) {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

        } else {
            ImmutableNavigationDrawer(
                drawerState = drawerState,
                navController = navController,
                signOutFunction = { viewModel.signOut { navController.navigate(LOGIN) } })
            {
                Scaffold(
                    topBar = {
                        TopAppBar(title = { Text(stringResource(id = R.string.app_name)) }, navigationIcon = {
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
                    Box(
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(innerPadding),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(70.dp))
                            Text(
                                text = stringResource(R.string.welcome, userName!!),
                                style = MaterialTheme.typography.bodyLarge,
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))

//                            MainSquareButton(
//                                text = stringResource(R.string.create_match),
//                                bgColor = Color.White,
//                                textColor = Purple40,
//                                borderColors = listOf(PurplePlayer, LightBluePlayer)
//                            ) {
//
//                            }

                            Button(onClick = { navController.navigate(CREATE_MATCH) }) {
                                Text(text = stringResource(R.string.create_match))
                            }

                            Button(onClick = { navController.navigate(MY_MATCHES) }) {
                                Text(text = stringResource(R.string.my_matches))
                            }

                            Button(onClick = { navController.navigate(MY_STATS) }) {
                                Text(text = stringResource(R.string.my_stats))
                            }

                            Button(onClick = { navController.navigate(FRIENDS) }) {
                                Text(text = stringResource(R.string.friends))
                            }
                        }
                        Button(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(vertical = 16.dp),
                            onClick = { viewModel.signOut { navController.navigate(LOGIN) } }) {
                            Text(text = stringResource(R.string.sign_out))
                        }
                    }

                }
            }
        }
    }
}






