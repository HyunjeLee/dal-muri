package com.dalmuri.dalmuri.presentation.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Long 타임스탬프를 "오전/오후 h:mm" 형식의 문자열로 변환합니다.
 */
fun Long.toFormattedTime(): String {
    val sdf = SimpleDateFormat("a h:mm", Locale.getDefault())
    return sdf.format(Date(this))
}

/**
 * Long 타임스탬프를 "2026. 02. 04." 형식의 문자열로 변환합니다.
 */
fun Long.toFormattedDate(): String {
    val sdf = SimpleDateFormat("yyyy. MM. dd.", Locale.getDefault())
    return sdf.format(Date(this))
}
