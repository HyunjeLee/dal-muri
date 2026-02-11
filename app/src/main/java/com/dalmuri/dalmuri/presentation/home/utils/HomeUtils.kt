package com.dalmuri.dalmuri.presentation.home.utils

import com.dalmuri.dalmuri.domain.model.Til
import com.dalmuri.dalmuri.presentation.utils.toFormattedDate
import java.util.Calendar

object HomeUtils {
    fun groupTilsByDate(tils: List<Til>): Map<String, List<Til>> {
        val today = Calendar.getInstance()
        val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }

        return tils.sortedByDescending { it.createdAt }.groupBy { til ->
            val tilDate = Calendar.getInstance().apply { timeInMillis = til.createdAt }
            when {
                isSameDay(today, tilDate) -> "Today"
                isSameDay(yesterday, tilDate) -> "Yesterday"
                else -> til.createdAt.toFormattedDate()
            }
        }
    }

    private fun isSameDay(
        cal1: Calendar,
        cal2: Calendar,
    ): Boolean = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}
