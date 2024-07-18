package com.ludicomm.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ludicomm.presentation.components.CustomTextField
import com.ludicomm.presentation.theme.LightGreenPlayer
import com.ludicomm.presentation.theme.RedPlayer
import com.ludicomm.presentation.viewmodel.FriendsViewModel

@Composable
fun FriendsScreen(
    viewModel: FriendsViewModel = hiltViewModel(),
    navRoutes: Map<String, () -> Unit>
) {

    val friendsList by viewModel.friendsList.collectAsState()
    val sentRequestList by viewModel.sentRequestList.collectAsState()
    val receivedRequestList by viewModel.receivedRequestList.collectAsState()
    val userQueryInput by viewModel.userQueryInput.collectAsState()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    Surface {

        LaunchedEffect(key1 = state) {
            if (state.isError.isNotBlank()) {
                Toast.makeText(context, state.isError, Toast.LENGTH_SHORT).show()
            } else if (state.isSuccess.isNotBlank()) {
                Toast.makeText(context, state.isSuccess, Toast.LENGTH_SHORT).show()
            }
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {

            Text(text = "Friend request:")
            Row(verticalAlignment = Alignment.CenterVertically) {
                CustomTextField(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(66f),
                    text = userQueryInput,
                    onTextChange = {
                        viewModel.changeInput(it)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = ""
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            viewModel.changeInput("")
                        }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear user query"
                            )
                        }
                    },
                    keyOpt = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                Button(modifier = Modifier.weight(33f), onClick = { viewModel.requestFriend() }) {
                    Text(text = "Request")
                }
                if (state.isLoading) {
                    CircularProgressIndicator()
                }

            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Pending Requests", fontSize = 18.sp)
            HorizontalDivider()
            Text(text = "Received:", fontSize = 17.sp)
            if (receivedRequestList.isEmpty()) Text(text = "no requests", fontSize = 15.sp)
            else {
                receivedRequestList.forEach {
                    Row(
                        modifier = Modifier.wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = it, fontSize = 15.sp)
                        Spacer(modifier = Modifier.width(100.dp))
                        IconButton(onClick = { viewModel.acceptFriendRequest(it) }) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                tint = LightGreenPlayer,
                                contentDescription = "Accept request"
                            )
                        }
                        IconButton(onClick = { viewModel.dismissReceivedRequest(it) }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                tint = RedPlayer,
                                contentDescription = "Dismiss request"
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Sent:", fontSize = 17.sp)
            if (sentRequestList.isEmpty()) Text(text = "no requests", fontSize = 15.sp)
            else {
                sentRequestList.forEach {
                    Row(
                        modifier = Modifier.wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "$it (awaiting response)", fontSize = 15.sp)
                        Spacer(modifier = Modifier.width(100.dp))
                        IconButton(onClick = { viewModel.dismissSentRequest(it) }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                tint = RedPlayer,
                                contentDescription = "Dismiss request"
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Friend List", fontSize = 18.sp)
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))
            if (friendsList.isEmpty()) Text(text = "forever alone :(", fontSize = 15.sp)
            LazyColumn {
                items(friendsList) {
                    Text(text = it, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

}