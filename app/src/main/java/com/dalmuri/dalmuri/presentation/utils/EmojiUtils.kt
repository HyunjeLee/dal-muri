package com.dalmuri.dalmuri.presentation.utils

fun Int.toEmoji(): String =
    when (this) {
        5 -> "🎉"
        4 -> "😊"
        3 -> "😐"
        2 -> "😓"
        else -> "😢"
    }
