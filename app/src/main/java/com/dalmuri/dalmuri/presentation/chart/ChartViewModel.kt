package com.dalmuri.dalmuri.presentation.chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dalmuri.dalmuri.domain.usecase.GetMonthlyChartDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ChartViewModel
    @Inject
    constructor(
        private val getMonthlyChartDataUseCase: GetMonthlyChartDataUseCase,
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
            getMonthlyChartDataUseCase(year, month)
                .map { result ->
                    result.fold(
                        onSuccess = { stats ->
                            ChartContract.State(
                                year = year,
                                month = month,
                                stats = stats,
                                isLoading = false,
                            )
                        },
                        onFailure = { error ->
                            _effect.emit(
                                ChartContract.SideEffect.ShowError(
                                    error.message ?: "Unknown error",
                                ),
                            )
                            ChartContract.State(
                                year = year,
                                month = month,
                                error = error.message,
                                isLoading = false,
                            )
                        },
                    )
                }.onStart {
                    emit(
                        ChartContract.State(
                            year = year,
                            month = month,
                            isLoading = true,
                        ),
                    )
                }
    }
