package com.dalmuri.dalmuri.presentation.chart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dalmuri.dalmuri.presentation.chart.components.*
import com.dalmuri.dalmuri.presentation.chart.dummy.dummyStats
import com.dalmuri.dalmuri.presentation.theme.DalmuriTheme

@Composable
fun ChartScreen(viewModel: ChartViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    ChartContent(
        state = uiState,
        onMonthChange = { delta ->
            viewModel.handleIntent(ChartContract.Intent.ChangeMonth(delta))
        },
    )
}

@Composable
private fun ChartContent(
    state: ChartContract.State,
    onMonthChange: (Int) -> Unit,
) {
    when (state.isLoading) {
        true -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        false -> {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 32.dp)
                        .padding(bottom = 32.dp),
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Top Navigation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = { onMonthChange(-1) }) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Previous Month",
                        )
                    }
                    Text(
                        text = "${state.month}월 월간 통계",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    IconButton(onClick = { onMonthChange(1) }) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Next Month",
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Donut Chart
                Text(text = "감정 비율", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                DonutChart(state.stats)

                Spacer(modifier = Modifier.height(48.dp))

                // Bar Chart
                Text(text = "감정별 TIL 개수", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                BarChart(state.stats)

                Spacer(modifier = Modifier.height(48.dp))

                // Line Chart
                Text(text = "일별 감정 점수 변화", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                LineChart(state.stats)

                Spacer(modifier = Modifier.height(48.dp))

                // AI Insight
                AiInsightCard(state.isAiLoading, state.aiChartSummary)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChartScreenPreview() {
    DalmuriTheme {
        ChartContent(state = ChartContract.State(stats = dummyStats), onMonthChange = {})
    }
}
