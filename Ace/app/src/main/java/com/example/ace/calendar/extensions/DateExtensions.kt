package com.example.ace.calendar.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Date.toSimpleDate(): String {
    val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return simpleDateFormat.format(this)
}