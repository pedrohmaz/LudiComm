package com.ludicomm.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(timestamp: Long) : String {

    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    val date = Date(timestamp)

    return sdf.format(date) ?: ""

}

fun removeEndBlanks(field: String): String {
    var fieldCopy = field

    while (fieldCopy.isNotEmpty() && fieldCopy.last()
            .isWhitespace()
    ) {
        fieldCopy = fieldCopy.dropLast(1)
    }
    return fieldCopy
}

fun Float.formatDecimal(): String {
    return if (this % 1f == 0.0f) {
        this.toInt().toString()
    } else {
        String.format(Locale.getDefault(),"%.1f", this)
    }
}