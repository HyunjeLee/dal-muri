package com.dalmuri.dalmuri.presentation.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dalmuri.dalmuri.domain.usecase.AnalyzeMonthlyReviewUseCase
import com.dalmuri.dalmuri.domain.usecase.GetTilsByMonthUseCase
import com.dalmuri.dalmuri.presentation.review.ReviewContract.Intent
import com.dalmuri.dalmuri.presentation.review.ReviewContract.SideEffect.*
import com.dalmuri.dalmuri.presentation.review.ReviewContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ReviewViewModel
    @Inject
    constructor(
        private val getTilsByMonthUseCase: GetTilsByMonthUseCase,
        private val analyzeMonthlyReviewUseCase: AnalyzeMonthlyReviewUseCase,
    ) : ViewModel() {
        private val year = MutableStateFlow(State().year)
        private val month = MutableStateFlow(State().month)

        private val _effect = MutableSharedFlow<ReviewContract.SideEffect>()
        val effect = _effect.asSharedFlow()

        val uiState =
            combine(year, month) { year, month -> year to month }
                .flatMapLatest { (year, month) -> fetchReviewData(year, month) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = State(),
                )

        fun handleIntent(intent: Intent) {
            when (intent) {
                is Intent.ChangeMonth -> {
                    var newMonth = month.value + intent.delta
                    var newYear = year.value
                    if (newMonth > 12) {
                        newMonth = 1
                        newYear++
                    } else if (newMonth < 1) {
                        newMonth = 12
                        newYear--
                    }
                    year.value = newYear
                    month.value = newMonth
                }
            }
        }

        private fun fetchReviewData(
            year: Int,
            month: Int,
        ) = flow {
            // 로딩 시작
            emit(State(year = year, month = month, isLoading = true))

            val tils =
                getTilsByMonthUseCase(year, month).first().getOrElse { error ->
                    _effect.emit(ShowError("기록을 가져오지 못했습니다."))
                    emit(
                        State(
                            year = year,
                            month = month,
                            isLoading = false,
                            error = error.message,
                        ),
                    )
                    return@flow
                }

            if (tils.isEmpty()) {
                emit(State(year = year, month = month, isLoading = false, error = "분석할 기록이 없습니다."))
                return@flow
            }

            // 2. 로컬 로딩 완료 -> AI 분석 로딩 시작
            emit(State(year = year, month = month, isLoading = false, isAiAnalysisLoading = true))

            analyzeMonthlyReviewUseCase(tils)
                .onSuccess { review ->
                    emit(
                        State(
                            year = year,
                            month = month,
                            reviewData = review,
                            isAiAnalysisLoading = false,
                        ),
                    )
                }.onFailure { error ->
                    val errorMessage = "AI 분석 중 오류가 발생했습니다."
                    _effect.emit(ShowError(errorMessage))
                    emit(
                        State(
                            year = year,
                            month = month,
                            error = error.message ?: errorMessage,
                            isAiAnalysisLoading = false,
                        ),
                    )
                }
        }
    }
