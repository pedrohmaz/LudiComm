package com.ludicomm.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ludicomm.R
import com.ludicomm.data.model.PlayerMatchData
import com.ludicomm.presentation.components.CustomTextField
import com.ludicomm.presentation.components.EditPlayerWindow
import com.ludicomm.presentation.components.PlayerMatchDisplay
import com.ludicomm.presentation.viewmodel.CreateMatchViewModel
import com.ludicomm.util.CreateMatchInputFields
import com.ludicomm.util.stateHandlers.CreateMatchState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMatchScreen(
    viewModel: CreateMatchViewModel = hiltViewModel(),
    onNavigateToMain: () -> Unit,
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    //inputs
    val gameQueryInput by viewModel.gameQueryInput.collectAsState()
    val gameThumbnail by viewModel.gameThumbnail.collectAsState()
    val nameInput by viewModel.nameInput.collectAsState()
    val factionInput by viewModel.colorOrFactionInput.collectAsState()
    val scoreInput by viewModel.scoreInput.collectAsState()
    val selectedColor by viewModel.selectedColor.collectAsState()

    //lists
    val suggestionList by viewModel.suggestionList.collectAsState()
    val playerList by viewModel.playerList.collectAsState()

    //toggles
    val toggleSuggestionList by viewModel.toggleSuggestionList.collectAsState()
    val toggleNoWinnerDialog by viewModel.toggleNoWinnerDialog.collectAsState()
    var toggleNewPlayerWindow by remember { mutableStateOf(false) }
    var toggleEditPlayerWindow by remember { mutableStateOf(false) }
    var toggleConfirmDeleteWindow by remember { mutableStateOf(false) }
    var editIndex by remember { mutableIntStateOf(0) }


    LaunchedEffect(key1 = gameQueryInput) {
        viewModel.createBGGSuggestions()
    }

    LaunchedEffect(key1 = state) {
        if (state.isError.isNotBlank()) {
            Toast.makeText(
                context,
                state.isError,
                Toast.LENGTH_SHORT
            ).show()
            viewModel.resetState()
        } else if (state.isSuccess == "Match submitted successfully") {
            Toast.makeText(
                context,
                state.isSuccess,
                Toast.LENGTH_SHORT
            ).show()
            onNavigateToMain()
            viewModel.resetState()
        }
    }

    Surface {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    title = {
                        Text("Create Match")
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                if (toggleNoWinnerDialog) {
                    AlertDialog(
                        title = { Text(text = "No winner selected") },
                        text = { Text(text = "Do you want to submit match without winners?") },
                        onDismissRequest = { viewModel.toggleNoWinnerDialog(false) },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    viewModel.submitNoWinnerMatch()
                                    viewModel.toggleNoWinnerDialog(false)
                                }) {
                                Text(text = "Confirm")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    viewModel.toggleNoWinnerDialog(false)
                                }) {
                                Text(text = "Dismiss")
                            }
                        }
                    )
                }

                if (toggleConfirmDeleteWindow) {

                    AlertDialog(
                        title = { Text(text = "Delete player") },
                        text = { Text(text = "Do you want to delete player?") },
                        onDismissRequest = { toggleConfirmDeleteWindow = false },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    viewModel.deletePlayer(playerList[editIndex])
                                    toggleConfirmDeleteWindow = false
                                }) {
                                Text(text = "Confirm")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    toggleConfirmDeleteWindow = false
                                }) {
                                Text(text = "Dismiss")
                            }
                        }
                    )

                }

                if (toggleNewPlayerWindow) {
                    viewModel.clearInputs()
                    EditPlayerWindow(
                        viewModel = viewModel,
                        onDismissRequest = { toggleNewPlayerWindow = false },
                        onConfirmation = {
                            viewModel.addPlayer(
                                PlayerMatchData(
                                    name = nameInput.ifBlank { "Player ${editIndex + 1}" },
                                    faction = factionInput,
                                    score = scoreInput.ifBlank { "0" },
                                    color = selectedColor?.toArgb()?.toString() ?: Black.toArgb()
                                        .toString()
                                )
                            )
                            toggleNewPlayerWindow = false
                        },
                        dialogTitle = "New player"
                    )
                }

                if (toggleEditPlayerWindow) {
                    EditPlayerWindow(
                        viewModel = viewModel,
                        onDismissRequest = { toggleEditPlayerWindow = false },
                        onConfirmation = {
                            viewModel.editPlayer(
                                editIndex, PlayerMatchData(
                                    name = nameInput.ifBlank { "Player ${editIndex + 1}" },
                                    faction = factionInput,
                                    score = scoreInput.ifBlank { "0" },
                                    color = selectedColor?.toArgb()?.toString() ?: Black.toArgb()
                                        .toString(),
                                    isWinner = playerList[editIndex].isWinner
                                )
                            )
                            toggleEditPlayerWindow = false
                        },
                        dialogTitle = "Edit player",
                    )
                }

                Text(modifier = Modifier.padding(), text = "Game:", fontSize = 22.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        text = gameQueryInput,
                        onTextChange = {
                            viewModel.changeInput(
                                it,
                                CreateMatchInputFields.GameQuery
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = ""
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                viewModel.changeInput(
                                    "",
                                    CreateMatchInputFields.GameQuery
                                )
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear game query"
                                )
                            }
                        },
                        keyOpt = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    Spacer(Modifier.width(8.dp))
                    if (state == CreateMatchState(isLoading = true)) CircularProgressIndicator(
                        modifier = Modifier.scale(0.8f)
                    )
                }
                Spacer(modifier = Modifier.height(11.dp))
                if (gameThumbnail.isNotBlank()) {
                    AsyncImage(
                        modifier = Modifier
                            .background(Green),
                        model = ImageRequest.Builder(context).data(gameThumbnail).build(),
                        contentDescription = "Selected game thumbnail"
                    )
                }
                Spacer(modifier = Modifier.height(11.dp))
                Text(text = "Players:", fontSize = 22.sp)
                Spacer(modifier = Modifier.height(22.dp))
                Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(Modifier.width(20.dp))
                        Text(text = "Name")
                        Spacer(modifier = Modifier.width(48.dp))
                        Text(text = "Color/Faction")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Score")
                    }
                    playerList.forEach {
                        PlayerMatchDisplay(player = it.name,
                            colorOrFaction = it.faction,
                            score = it.score.ifBlank { "0" },
                            color = Color(it.color.toInt()),
                            onDeleteClick =
                            {
                                editIndex = playerList.indexOf(it)
                                toggleConfirmDeleteWindow = true
                            },
                            onEditClick = {
                                editIndex = playerList.indexOf(it)
                                viewModel.changeInput(it.name, CreateMatchInputFields.Name)
                                viewModel.changeInput(
                                    it.faction,
                                    CreateMatchInputFields.ColorOrFaction
                                )
                                viewModel.changeInput(it.score, CreateMatchInputFields.Score)
                                viewModel.changeSelectedColor(Color(it.color.toInt()))
                                toggleEditPlayerWindow = true
                            },
                            onWinnerStarClick = { it.isWinner = !it.isWinner }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(36.dp))
                FilledTonalIconButton(modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(36.dp)
                    .clip(CircleShape), onClick = {
                    editIndex = playerList.size
                    toggleNewPlayerWindow = true
                })
                {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Icon")
                }
                Spacer(modifier = Modifier.height(64.dp))
                Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = { viewModel.submitMatch() })
                {
                    Text(text = "Submit Match")
                }
            }
            if (toggleSuggestionList) {
                LazyColumn(
                    Modifier
                        .padding(8.dp)
                        .offset(y = 156.dp)
                        .background(White)
                ) {
                    items(suggestionList) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(width = 0.5.dp, color = Black, shape = RectangleShape)
                                .clickable {
                                    viewModel.changeInput(it[0], CreateMatchInputFields.GameQuery)
                                    viewModel.toggleOnGoingQuery(false)
                                    viewModel.toggleSuggestionList(false)
                                    viewModel.setLastGameCLicked(it[0])
                                    viewModel.changeGameThumbnail(it[1])
                                }) {
                            Row {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(it[1])
                                        .crossfade(true)
                                        .error(R.drawable.no_image)
                                        .build(), contentDescription = "thumbnail",
                                    modifier = Modifier.size(80.dp)
                                )
                                Text(
                                    modifier = Modifier.fillMaxHeight(),
                                    text = it[0],
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}






