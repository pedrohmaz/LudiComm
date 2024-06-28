package com.ludicomm.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.unit.dp

@Composable
fun ColorButton(color: Color, borderColor: Color = Black, onCLick: () -> Unit) {

    OutlinedButton(
        modifier = Modifier.size(32.dp, 32.dp),
        colors = ButtonColors(color, borderColor, Color.Red, Black),
        border = BorderStroke(
            2.dp,
            borderColor
        ),
        shape = RoundedCornerShape(16),
        onClick = { onCLick() }) {

    }

}