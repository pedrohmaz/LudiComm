package com.ludicomm.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ludicomm.R
import com.ludicomm.presentation.theme.YellowPlayer
import com.ludicomm.presentation.theme.DeactivatedGrey
import okhttp3.internal.checkOffsetAndCount

@Composable
fun PlayerMatchDisplay(
    player: String,
    colorOrFaction: String,
    score: String = "0",
    color: Color,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    onWinnerStarClick: () -> Unit,
    winnerIcon: Boolean

) {

    Row(
        Modifier
            .fillMaxWidth()
            .padding(2.dp),
        //horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(modifier = Modifier.weight(10f), onClick = {
            onWinnerStarClick()
        }) {
            if (winnerIcon) {
                Icon(modifier = Modifier.offset(y = (-3).dp),
                    painter = painterResource(R.drawable.ic_crown),
                    contentDescription = "Winner crown",
                    tint = YellowPlayer
                )
            } else {
                Icon(modifier = Modifier.offset(y = (-3).dp),
                    painter = painterResource(R.drawable.ic_crown),
                    contentDescription = "Deactivated crown",
                    tint = DeactivatedGrey
                )
            }
        }
        Spacer(modifier = Modifier.weight(5f))
        Text(modifier = Modifier.weight(27.5f), text = player, fontSize = 17.sp)
        Text(
            modifier = Modifier.weight(33.5f),
            text = colorOrFaction,
            fontSize = 18.sp,
            fontWeight = Bold,
            color = color
        )
        Text(modifier = Modifier.weight(12.5f), text = score, fontSize = 17.sp)
        IconButton(modifier = Modifier.weight(10f), onClick = { onEditClick() }) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = ""
            )
        }
        IconButton(modifier = Modifier.weight(10f), onClick = { onDeleteClick() }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = ""
            )
        }
    }
}