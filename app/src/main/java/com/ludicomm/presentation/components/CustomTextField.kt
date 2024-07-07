package com.ludicomm.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun CustomTextField(
    text: String,
    onTextChange: (String) -> Unit,
    label: String = "",
    keyOpt: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon:  @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = text,
        label = { Text(text = label) },
        keyboardOptions = keyOpt,
        onValueChange = {newText: String -> onTextChange(newText) },
        singleLine = true,
        trailingIcon = trailingIcon
    )
}

@Composable
fun TextFieldWithTwoTrailingIcons(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    trailingIcon1: @Composable (() -> Unit)? = null,
    trailingIcon2: @Composable (() -> Unit)? = null,
    keyOpt: KeyboardOptions
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 72.dp), // adjust padding for trailing icons
            trailingIcon = null // Remove the default trailing icon
        )
        if (trailingIcon1 != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 36.dp) // Position first icon
            ) {
                trailingIcon1()
            }
        }
        if (trailingIcon2 != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
            ) {
                trailingIcon2()
            }
        }
    }
}