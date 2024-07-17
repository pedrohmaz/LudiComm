package com.ludicomm.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
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

    Surface {

        Column(Modifier.fillMaxSize()) {

            Text(text = "Friend request:")
            Row(verticalAlignment = Alignment.CenterVertically) {
                CustomTextField(
                    modifier = Modifier
                        .width(270.dp)
                        .padding(8.dp),
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

                Button(onClick = { viewModel.requestFriend() }) {
                    Text(text = "Request")
                }

            }

            Text(text = "Pending Requests")
            HorizontalDivider()
            Text(text = "Received")
            Column {
                receivedRequestList.forEach {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = it)
                        Spacer(modifier = Modifier.width(100.dp))
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                tint = LightGreenPlayer,
                                contentDescription = "Accept request"
                            )
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                tint = RedPlayer,
                                contentDescription = "Dismiss request"
                            )
                        }
                    }
                }
            }
            HorizontalDivider()
            Text(text = "Sent")
            Column {
                sentRequestList.forEach {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "$it (awaiting response)")
                        Spacer(modifier = Modifier.width(100.dp))
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                tint = RedPlayer,
                                contentDescription = "Dismiss request"
                            )
                        }
                    }
                }
            }
            HorizontalDivider()
            LazyColumn {
                items(friendsList) {
                    Text(text = it)
                    HorizontalDivider()
                }
            }
        }
    }

}