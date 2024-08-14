package com.ludicomm.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ludicomm.presentation.theme.PinkPlayer

@Composable
fun MainSquareButton(
    text: String,
    bgColor: Color,
    textColor: Color,
    borderColors: List<Color>,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(130.dp)
            .background(bgColor, shape = RoundedCornerShape(8.dp))
            .border(
                width = 3.dp,
                brush = Brush.linearGradient(
                    colors = borderColors
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick),
    ) {
        Icon(
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.TopCenter)
                .padding(top = (16).dp),
            imageVector = Icons.Default.Face,
            tint = PinkPlayer,
            contentDescription = "Icon"
        )
        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .padding(start = 16.dp),
            text = text,
            color = textColor,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Clip
        )
    }
}
