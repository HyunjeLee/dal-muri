package com.dalmuri.dalmuri.data.local

import android.util.Log
import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RoomConverters {
    private val json =
        Json {
            ignoreUnknownKeys = true // 미확인 키 무시
            coerceInputValues = true // 타입 불일치 시 기본값으로 치환
        }

    @TypeConverter
    fun fromStringList(value: List<String>): String = json.encodeToString(value)

    @TypeConverter
    fun toStringList(value: String): List<String> =
        try {
            json.decodeFromString(value)
        } catch (e: Exception) {
            Log.e("RoomConverters", "Failed to decode JSON to List<String>. Value: $value", e)
            emptyList()
        }
}
