package com.dalmuri.dalmuri.presentation.chart

import com.dalmuri.dalmuri.domain.model.MonthlyChartData
import java.time.LocalDate
import java.time.LocalDateTime

class ChartContract {
    data class State(
        val isLoading: Boolean = false,
        val year: Int = LocalDate.now().year,
        val month: Int = LocalDateTime.now().monthValue,
        val stats: MonthlyChartData? = null,
        val error: String? = null,
    )

    sealed class Intent {
        object LoadStats : Intent()

        data class ChangeMonth(
            val delta: Int,
        ) : Intent()
    }

    sealed class SideEffect {
        data class ShowError(
            val message: String,
        ) : SideEffect()
    }
}
