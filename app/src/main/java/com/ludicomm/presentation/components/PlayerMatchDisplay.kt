package com.ludicomm.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlayerMatchDisplay(
    player: String,
    colorOrFaction: String,
    score: String = "0",
    color: Color,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(modifier = Modifier.width(108.dp), text = player, fontSize = 18.sp)
        Text(modifier = Modifier.width(108.dp), text = colorOrFaction, fontSize = 18.sp, color = color)
        Spacer(modifier = Modifier.width(16.dp))
        Text(modifier = Modifier.width(65.dp), text = score, fontSize = 18.sp)
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