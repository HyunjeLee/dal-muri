package com.dalmuri.dalmuri.presentation.chart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dalmuri.dalmuri.domain.model.MonthlyChartData
import com.dalmuri.dalmuri.domain.usecase.AnalyzeMonthlyChartDataUseCase
import com.dalmuri.dalmuri.domain.usecase.GetChartSummaryUseCase
import com.dalmuri.dalmuri.domain.usecase.GetMonthlyChartDataUseCase
import com.dalmuri.dalmuri.domain.usecase.SaveChartSummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ChartViewModel
    @Inject
    constructor(
        private val getMonthlyChartDataUseCase: GetMonthlyChartDataUseCase,
        private val analyzeMonthlyChartDataUseCase: AnalyzeMonthlyChartDataUseCase,
        private val getChartSummaryUseCase: GetChartSummaryUseCase,
        private val saveChartSummaryUseCase: SaveChartSummaryUseCase,
    ) : ViewModel() {
        private val year = MutableStateFlow(ChartContract.State().year)
        private val month = MutableStateFlow(ChartContract.State().month)

        private val _effect = MutableSharedFlow<ChartContract.SideEffect>()
        val effect = _effect.asSharedFlow()

        val uiState =
            combine(year, month) { year, month -> year to month }
                .flatMapLatest { (year, month) -> fetchChartData(year, month) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = ChartContract.State(),
                )

        fun handleIntent(intent: ChartContract.Intent) {
            when (intent) {
                is ChartContract.Intent.ChangeMonth -> {
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

        private fun fetchChartData(
            year: Int,
            month: Int,
        ): Flow<ChartContract.State> =
            flow {
                // 1. 초기 상태
                var state =
                    ChartContract.State(
                        year = year,
                        month = month,
                        isLoading = true,
                        isAiLoading = true,
                    )
                emit(state)

                // 2. 차트 데이터 가져오기
                getMonthlyChartDataUseCase(year, month).collect { result ->
                    state = updateStateWithResult(result, state)
                    emit(state)

                    // 3. AI 데이터 가져오기 또는 생성
                    if (result.isSuccess) {
                        state = processAiSummary(state)
                        emit(state)
                    }
                }
            }

        private fun updateStateWithResult(
            result: Result<MonthlyChartData>,
            state: ChartContract.State,
        ): ChartContract.State =
            result.fold(
                onSuccess = { stats ->
                    state.copy(isLoading = false, stats = stats)
                },
                onFailure = { error ->
                    viewModelScope.launch {
                        _effect.emit(
                            ChartContract.SideEffect.ShowError(
                                error.message ?: "Unknown error",
                            ),
                        )
                    }
                    state.copy(isLoading = false, isAiLoading = false, error = error.message)
                },
            )

        private suspend fun processAiSummary(state: ChartContract.State): ChartContract.State {
            val stats = state.stats ?: return state.copy(isAiLoading = false)

            if (stats.totalTilCount == 0) {
                return state.copy(
                    aiChartSummary = "이번 달 기록이 아직 없네요. 소중한 하루를 기록해보세요!",
                    isAiLoading = false,
                )
            }

            return getChartSummaryUseCase(stats.yearMonth).fold(
                onSuccess = { summary ->
                    when (summary.isNullOrEmpty()) {
                        true -> fetchAiAnalyze(state)
                        false -> state.copy(aiChartSummary = summary, isAiLoading = false)
                    }
                },
                onFailure = { error ->
                    viewModelScope.launch {
                        _effect.emit(
                            ChartContract.SideEffect.ShowError(
                                error.message ?: "Unknown error",
                            ),
                        )
                    }
                    state.copy(isAiLoading = false, error = error.message)
                },
            )
        }

        private suspend fun fetchAiAnalyze(state: ChartContract.State): ChartContract.State {
            val stats = state.stats ?: return state.copy(isAiLoading = false)

            return analyzeMonthlyChartDataUseCase(
                totalTilCount = stats.totalTilCount,
                averageEmotionScore = stats.averageEmotionScore,
                emotionCounts = stats.emotionCounts,
            ).fold(
                onSuccess = { summary ->
                    saveChartSummary(
                        yearMonth = stats.yearMonth.toString(),
                        summary = summary,
                    )

                    state.copy(isAiLoading = false, aiChartSummary = summary)
                },
                onFailure = {
                    viewModelScope.launch {
                        _effect.emit(ChartContract.SideEffect.ShowError("AI 분석에 실패했습니다."))
                    }
                    state.copy(isAiLoading = false)
                },
            )
        }

        private suspend fun saveChartSummary(
            yearMonth: String,
            summary: String,
        ) {
            saveChartSummaryUseCase(yearMonth, summary).fold(
                onSuccess = { Log.d("ChartViewModel", "Chart summary saved successfully") },
                onFailure = { Log.e("ChartViewModel", "Failed to save chart summary", it) },
            )
        }
    }
