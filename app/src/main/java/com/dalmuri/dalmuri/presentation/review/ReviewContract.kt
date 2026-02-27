package com.dalmuri.dalmuri.presentation.review

import com.dalmuri.dalmuri.domain.model.MonthlyReview
import java.time.LocalDate
import java.time.LocalDateTime

class ReviewContract {
    data class State(
        val isLoading: Boolean = false,
        val year: Int = LocalDate.now().year,
        val month: Int = LocalDateTime.now().monthValue,
        val isAiAnalysisLoading: Boolean = false,
        val reviewData: MonthlyReview? = null,
        val error: String? = null,
    )

    sealed class Intent {
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
