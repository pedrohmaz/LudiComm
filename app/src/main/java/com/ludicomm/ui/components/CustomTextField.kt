package com.ludicomm.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ludicomm.util.InputField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    text: String,
    onTextChange: (String) -> Unit,
    label: String = "",
    keyOpt: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = text,
        label = { Text(text = label) },
        keyboardOptions = keyOpt,
        onValueChange = {newText: String -> onTextChange(newText) },
        singleLine = true
    )
}