package com.dalmuri.dalmuri.presentation.chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dalmuri.dalmuri.domain.usecase.GetMonthlyChartDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChartViewModel
    @Inject
    constructor(
        private val getMonthlyChartDataUseCase: GetMonthlyChartDataUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ChartContract.State())
        val uiState = _uiState.asStateFlow()

        private val _effect = MutableSharedFlow<ChartContract.SideEffect>()
        val effect = _effect.asSharedFlow()

        init {
            handleIntent(ChartContract.Intent.LoadStats)
        }

        fun handleIntent(intent: ChartContract.Intent) {
            when (intent) {
                is ChartContract.Intent.LoadStats -> loadStats()
                is ChartContract.Intent.ChangeMonth -> {
                    _uiState.update {
                        var newMonth = it.month + intent.delta
                        var newYear = it.year
                        if (newMonth > 12) {
                            newMonth = 1
                            newYear++
                        } else if (newMonth < 1) {
                            newMonth = 12
                            newYear--
                        }
                        it.copy(year = newYear, month = newMonth)
                    }
                    loadStats()
                }
            }
        }

        private fun loadStats() {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }

                val result = getMonthlyChartDataUseCase(uiState.value.year, uiState.value.month)
                result
                    .onSuccess { stats ->
                        _uiState.update { it.copy(isLoading = false, stats = stats, error = null) }
                    }.onFailure { error ->
                        _uiState.update { it.copy(isLoading = false, error = error.message) }
                        _effect.emit(
                            ChartContract.SideEffect.ShowError(
                                error.message ?: "Unknown error",
                            ),
                        )
                    }
            }
        }
    }
