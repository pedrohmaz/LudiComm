package com.ludicomm.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
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
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ludicomm.R
import com.ludicomm.data.model.PlayerMatchData
import com.ludicomm.presentation.components.CustomTextField
import com.ludicomm.presentation.components.EditPlayerWindow
import com.ludicomm.presentation.components.ImmutableNavigationDrawer
import com.ludicomm.presentation.components.PlayerMatchDisplay
import com.ludicomm.presentation.viewmodel.CreateMatchViewModel
import com.ludicomm.util.CreateMatchInputFields
import com.ludicomm.util.removeEndBlanks
import com.ludicomm.util.stateHandlers.CreateMatchState
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMatchScreen(
    viewModel: CreateMatchViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
    val friendsList by viewModel.friendsList.collectAsState()

    //toggles
    val toggleSuggestionList by viewModel.toggleSuggestionList.collectAsState()
    val toggleNoWinnerDialog by viewModel.toggleNoWinnerDialog.collectAsState()
    val toggleNotAUserDialog by viewModel.toggleNotAUserDialog.collectAsState()
    val toggleRepeatedPlayerDialog by viewModel.toggleRepeatedPlayerDialog.collectAsState()
    val toggleNewPlayerWindow by viewModel.toggleNewPlayerWindow.collectAsState()
    val toggleEditPlayerWindow by viewModel.toggleEditPlayerWindow.collectAsState()
    val toggleConfirmDeleteDialog by viewModel.toggleConfirmDeleteDialog.collectAsState()


    val currentUser by viewModel.currentUser.collectAsState()
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
            navController.navigate(MAIN)
            viewModel.resetState()
        }
    }

    Surface {
        ImmutableNavigationDrawer(
            drawerState = drawerState,
            navController = navController,
            signOutFunction = { viewModel.signOut { navController.navigate(LOGIN) } }
        )
        {
            Scaffold(
                topBar = {
                    TopAppBar(title = { Text(text = "Create Match") }, navigationIcon = {
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


                    if (toggleConfirmDeleteDialog) {
                        AlertDialog(
                            title = { Text(text = "Delete player") },
                            text = { Text(text = "Do you want to delete player?") },
                            onDismissRequest = { viewModel.toggleConfirmDeleteDialog(false) },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        viewModel.deletePlayer(editIndex)
                                        viewModel.toggleConfirmDeleteDialog(false)
                                    }) {
                                    Text(text = "Confirm")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = {
                                        viewModel.toggleConfirmDeleteDialog(false)
                                    }) {
                                    Text(text = "Dismiss")
                                }
                            }
                        )

                    }

                    if (toggleNewPlayerWindow) {
                        EditPlayerWindow(
                            viewModel = viewModel,
                            onDismissRequest = { viewModel.toggleNewPlayerWindow(false) },
                            onConfirmation = {
                                val updatedName = removeEndBlanks(nameInput)
                                val updatedFaction = removeEndBlanks(factionInput)
                                if (friendsList.contains(nameInput) && !playerList.any { it.name == nameInput }
                                    || nameInput == currentUser && !playerList.any { it.name == nameInput }) {
                                    viewModel.addPlayer(
                                        PlayerMatchData(
                                            name = updatedName.ifEmpty { "Player ${editIndex + 1}" },
                                            faction = updatedFaction,
                                            score = scoreInput.ifBlank { "0" },
                                            color = selectedColor?.toArgb()?.toString()
                                                ?: Black.toArgb()
                                                    .toString()
                                        )
                                    )
                                    viewModel.clearInputs()
                                    viewModel.toggleNewPlayerWindow(false)
                                } else if (!friendsList.contains(nameInput)) viewModel.toggleNotAUserDialog(
                                    true
                                )
                                else viewModel.toggleRepeatedPlayerDialog(true)
                            },
                            friendsList = friendsList,
                            dialogTitle = "New player"
                        )
                    }

                    if (toggleEditPlayerWindow) {
                        EditPlayerWindow(
                            viewModel = viewModel,
                            onDismissRequest = { viewModel.toggleEditPlayerWindow(false) },
                            onConfirmation = {
                                val updatedName = removeEndBlanks(nameInput)
                                val updatedFaction = removeEndBlanks(factionInput)
                                viewModel.editPlayer(
                                    editIndex, PlayerMatchData(
                                        name = updatedName.ifBlank { "Player ${editIndex + 1}" },
                                        faction = updatedFaction,
                                        score = scoreInput.ifBlank { "0" },
                                        color = selectedColor?.toArgb()?.toString()
                                            ?: Black.toArgb()
                                                .toString(),
                                        isWinner = playerList[editIndex].isWinner
                                    )
                                )
                                viewModel.clearInputs()
                                viewModel.toggleEditPlayerWindow(false)
                            },
                            friendsList = friendsList,
                            dialogTitle = "Edit player",
                        )
                    }

                    if (toggleNotAUserDialog) {
                        AlertDialog(
                            title = { Text(text = "Player is not a registered user") },
                            text = { Text(text = "Do you want to add player as a guest?") },
                            onDismissRequest = { viewModel.toggleNotAUserDialog(false) },
                            confirmButton = {
                                TextButton(onClick = {
                                    val updatedName = removeEndBlanks(nameInput)
                                    val updatedFaction = removeEndBlanks(factionInput)
                                    viewModel.addPlayer(
                                        PlayerMatchData(
                                            name = updatedName.ifEmpty { "Player ${editIndex + 1}" } + " (guest)",
                                            faction = updatedFaction,
                                            score = scoreInput.ifBlank { "0" },
                                            color = selectedColor?.toArgb()?.toString()
                                                ?: Black.toArgb()
                                                    .toString()
                                        )
                                    )
                                    viewModel.toggleNotAUserDialog(false)
                                    viewModel.toggleNewPlayerWindow(false)
                                    viewModel.clearInputs()
                                }) {
                                    Text(text = "Confirm")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { viewModel.toggleNotAUserDialog(false) }) {
                                    Text(text = "Dismiss")
                                }
                            })
                    }

                    if (toggleRepeatedPlayerDialog) {
                        AlertDialog(
                            title = { Text(text = "Player is already registered") },
                            text = { Text(text = "Do you want to add a copy of the player as a guest?") },
                            onDismissRequest = { viewModel.toggleRepeatedPlayerDialog(false) },
                            confirmButton = {
                                TextButton(onClick = {
                                    val updatedName = removeEndBlanks(nameInput)
                                    val updatedFaction = removeEndBlanks(factionInput)
                                    viewModel.addPlayer(
                                        PlayerMatchData(
                                            name = updatedName.ifEmpty { "Player ${editIndex + 1}" } + " (guest)",
                                            faction = updatedFaction,
                                            score = scoreInput.ifBlank { "0" },
                                            color = selectedColor?.toArgb()?.toString()
                                                ?: Black.toArgb()
                                                    .toString()
                                        )
                                    )
                                    viewModel.toggleRepeatedPlayerDialog(false)
                                    viewModel.toggleNewPlayerWindow(false)
                                    viewModel.clearInputs()
                                }) {
                                    Text(text = "Confirm")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { viewModel.toggleRepeatedPlayerDialog(false) }) {
                                    Text(text = "Dismiss")
                                }
                            })
                    }

                    Text(
                        modifier = Modifier.padding(),
                        text = "Game:",
                        fontSize = 22.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomTextField(
                            modifier = Modifier
                                .width(500.dp)
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
                                    viewModel.toggleSuggestionList(false)
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
                    Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically){
                        Text(text = "Powered by BoardGameGeek®")
                        Spacer(modifier = Modifier.width(8.dp))
                        Image(modifier = Modifier.size(28.dp),painter = painterResource(id = R.drawable.bgg_logo), contentDescription = "BGG logo")
                    }
                    if (gameThumbnail.isNotBlank()) {
                        AsyncImage(
                            modifier = Modifier
                                .padding(horizontal = 32.dp, vertical = 20.dp)
                                .scale(1.8f),
                            model = ImageRequest.Builder(context).data(gameThumbnail).build(),
                            contentDescription = "Selected game thumbnail"
                        )
                    }
                    Spacer(modifier = Modifier.height(11.dp))
                    Text(text = "Players:", fontSize = 22.sp)
                    Spacer(modifier = Modifier.height(22.dp))
                    Column(
                        Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            //horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.weight(16f))
                            Text(modifier = Modifier.weight(27.5f), text = "Name")
                            //   Spacer(modifier = Modifier.weight(48.dp))
                            Text(modifier = Modifier.weight(32f), text = "Color/Faction")
                            // Spacer(modifier = Modifier.weight(8.dp))
                            Text(modifier = Modifier.weight(14f), text = "Score")
                            Spacer(modifier = Modifier.weight(20f))
                        }
                        playerList.forEach {
                            PlayerMatchDisplay(
                                player = it.name,
                                colorOrFaction = it.faction,
                                score = it.score.ifBlank { "0" },
                                color = Color(it.color.toInt()),
                                onDeleteClick =
                                {
                                    viewModel.toggleConfirmDeleteDialog(true)
                                },
                                onEditClick = {
                                    editIndex = playerList.indexOf(it)
                                    viewModel.changeInput(it.name, CreateMatchInputFields.Name)
                                    viewModel.changeInput(
                                        it.faction,
                                        CreateMatchInputFields.ColorOrFaction
                                    )
                                    viewModel.changeInput(
                                        it.score,
                                        CreateMatchInputFields.Score
                                    )
                                    viewModel.changeSelectedColor(Color(it.color.toInt()))
                                    viewModel.toggleEditPlayerWindow(true)
                                },
                                onWinnerStarClick = {
                                    it.isWinner = !it.isWinner
                                    viewModel.updatePlayerList()   //horrible solution for triggering recomposition :(
                                },
                                winnerIcon = it.isWinner
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(36.dp))
                    FilledTonalIconButton(modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(36.dp)
                        .clip(CircleShape), onClick = {
                        editIndex = playerList.size
                        viewModel.toggleNewPlayerWindow(true)
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
                                    .border(
                                        width = 0.5.dp,
                                        color = Black,
                                        shape = RectangleShape
                                    )
                                    .clickable {
                                        viewModel.changeInput(
                                            it[0],
                                            CreateMatchInputFields.GameQuery
                                        )
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
}






