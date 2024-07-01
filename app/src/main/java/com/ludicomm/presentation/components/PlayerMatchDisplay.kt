package com.ludicomm.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ludicomm.presentation.theme.YellowPlayer

@Composable
fun PlayerMatchDisplay(
    player: String,
    colorOrFaction: String,
    score: String = "0",
    color: Color,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    onWinnerStarClick: () -> Unit,

) {

    var toggleWinnerIcon by remember { mutableStateOf(false) }

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            toggleWinnerIcon = !toggleWinnerIcon
            onWinnerStarClick()
        }) {
            if (toggleWinnerIcon) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Winner star",
                    tint = YellowPlayer
                )
            } else {
                Icon(
                    imageVector = Icons.TwoTone.Star,
                    contentDescription = "Outlined star"
                )
            }
        }

        Text(modifier = Modifier.width(108.dp), text = player, fontSize = 18.sp)
        Text(
            modifier = Modifier.width(108.dp),
            text = colorOrFaction,
            fontSize = 18.sp,
            color = color
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(modifier = Modifier.width(44.dp), text = score, fontSize = 18.sp)
        IconButton(onClick = { onEditClick() }) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = ""
            )
        }
        IconButton(onClick = { onDeleteClick() }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = ""
            )
        }
    }
}