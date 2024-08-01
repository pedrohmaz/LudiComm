package com.ludicomm.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ludicomm.presentation.theme.BlackPlayer
import com.ludicomm.presentation.theme.DarkBluePlayer
import com.ludicomm.presentation.theme.DarkGreenPlayer
import com.ludicomm.presentation.theme.GreyPlayer
import com.ludicomm.presentation.theme.LightBluePlayer
import com.ludicomm.presentation.theme.LightGreenPlayer
import com.ludicomm.presentation.theme.OrangePlayer
import com.ludicomm.presentation.theme.PinkPlayer
import com.ludicomm.presentation.theme.PurplePlayer
import com.ludicomm.presentation.theme.RedPlayer
import com.ludicomm.presentation.theme.WhitePlayer
import com.ludicomm.presentation.theme.YellowPlayer
import com.ludicomm.presentation.viewmodel.CreateMatchViewModel
import com.ludicomm.util.CreateMatchInputFields

@Composable
fun EditPlayerWindow(
    viewModel: CreateMatchViewModel,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    friendsList: List<String>,
    dialogTitle: String,
) {
    val context = LocalContext.current
    Dialog(onDismissRequest) {

        val nameInput by viewModel.nameInput.collectAsState()
        val colorOrFactionInput by viewModel.colorOrFactionInput.collectAsState()
        val score by viewModel.scoreInput.collectAsState()
        val selectedColor: Color? by viewModel.selectedColor.collectAsState()

        var friendSelected by remember { mutableStateOf("") }
        val currentUser by viewModel.currentUser.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 4.dp,
                    vertical =
                    if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                        130.dp
                    else 0.dp
                )
                .clip(RoundedCornerShape(8))
                .background(White),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = dialogTitle, fontSize = 24.sp)

            OutlinedTextField(value = nameInput,
                singleLine = true,
                onValueChange = {
                    viewModel.changeInput(it, CreateMatchInputFields.Name)
                },
                label = {
                    Text(
                        text = "Player Name"
                    )
                })

            if (nameInput.isNotBlank() && friendSelected != nameInput) {
                viewModel.addCurrentUserAsFriend()
                LazyColumn(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 16.dp)
                        .offset(y = (-16).dp)
                        .background(MaterialTheme.colorScheme.secondary)
                ) {
                    items(friendsList) { friend ->
                        if (friend.lowercase().contains(Regex(nameInput.lowercase()))) {
                            Column(modifier = Modifier.clickable {
                                viewModel.changeInput(friend, CreateMatchInputFields.Name)
                                friendSelected = friend
                            }) {
                                Text(
                                    text = if (friend == currentUser) friend.plus(" (you)")
                                    else friend,
                                    color = White
                                )
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }

            OutlinedTextField(
                value = colorOrFactionInput,
                textStyle = TextStyle(fontWeight = Bold),
                onValueChange = {
                    viewModel.changeInput(
                        it,
                        CreateMatchInputFields.ColorOrFaction
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    selectedColor ?: Black,
                    selectedColor ?: Black
                ),
                singleLine = true,
                label = {
                    Text(
                        text = "Color or Faction"
                    )
                })

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

                ColorButton(
                    color = RedPlayer,
                    borderColor = if (selectedColor == RedPlayer) Black else White
                ) {
                    viewModel.changeSelectedColor(RedPlayer)
                }
                ColorButton(
                    color = DarkBluePlayer,
                    borderColor = if (selectedColor == DarkBluePlayer) Black else White
                ) {
                    viewModel.changeSelectedColor(DarkBluePlayer)
                }
                ColorButton(
                    color = DarkGreenPlayer,
                    borderColor = if (selectedColor == DarkGreenPlayer) Black else White
                ) {
                    viewModel.changeSelectedColor(DarkGreenPlayer)
                }
                ColorButton(
                    color = YellowPlayer,
                    borderColor = if (selectedColor == YellowPlayer) Black else White
                ) {
                    viewModel.changeSelectedColor(YellowPlayer)
                }

                ColorButton(
                    color = GreyPlayer,
                    borderColor = if (selectedColor == GreyPlayer) Black else White
                ) {
                    viewModel.changeSelectedColor(GreyPlayer)
                }

                ColorButton(
                    color = BlackPlayer,
                    borderColor = if (selectedColor == BlackPlayer) Color.LightGray else White
                ) {
                    viewModel.changeSelectedColor(BlackPlayer)
                }

            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

                ColorButton(
                    color = OrangePlayer,
                    borderColor = if (selectedColor == OrangePlayer) Black else White
                ) {
                    viewModel.changeSelectedColor(OrangePlayer)
                }
                ColorButton(
                    color = LightBluePlayer,
                    borderColor = if (selectedColor == LightBluePlayer) Black else White
                ) {
                    viewModel.changeSelectedColor(LightBluePlayer)
                }
                ColorButton(
                    color = LightGreenPlayer,
                    borderColor = if (selectedColor == LightGreenPlayer) Black else White
                ) {
                    viewModel.changeSelectedColor(LightGreenPlayer)
                }
                ColorButton(
                    color = PurplePlayer,
                    borderColor = if (selectedColor == PurplePlayer) Black else White
                ) {
                    viewModel.changeSelectedColor(PurplePlayer)
                }

                ColorButton(
                    color = PinkPlayer,
                    borderColor = if (selectedColor == PinkPlayer) Black else White
                ) {
                    viewModel.changeSelectedColor(PinkPlayer)
                }

                ColorButton(
                    color = WhitePlayer,
                    borderColor = if (selectedColor == WhitePlayer) Black else White
                ) {
                    viewModel.changeSelectedColor(WhitePlayer)
                }

            }

            OutlinedTextField(
                value = score,
                onValueChange = { viewModel.changeInput(it, CreateMatchInputFields.Score) },
                singleLine = true,
                label = {
                    Text(
                        text = "Score"
                    )
                }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )


            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                TextButton(
                    onClick = {
                        onConfirmation()
                    }
                ) {
                    Text("Confirm", fontSize = 16.sp)
                }

                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text("Dismiss", fontSize = 16.sp)
                }
            }
        }
    }
}


