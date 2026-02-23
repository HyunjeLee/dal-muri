package com.dalmuri.dalmuri.presentation.chart.components

import android.R.attr.y
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dalmuri.dalmuri.domain.model.Emotion
import com.dalmuri.dalmuri.domain.model.MonthlyChartData
import com.dalmuri.dalmuri.presentation.chart.dummy.dummyStats
import com.dalmuri.dalmuri.presentation.chart.utils.ChartDefaults
import com.dalmuri.dalmuri.presentation.theme.DalmuriTheme
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import kotlinx.coroutines.runBlocking

@Composable
private fun BarChart(
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier = Modifier,
) {
    CartesianChartHost(
        chart =
            rememberCartesianChart(
                rememberColumnCartesianLayer(
                    rangeProvider = ChartDefaults.rangeProvider,
                ),
                startAxis =
                    VerticalAxis.rememberStart(
                        guideline = null,
                        valueFormatter = ChartDefaults.integerValueFormatter,
                        itemPlacer = VerticalAxis.ItemPlacer.step({ 1.0 }),
                    ),
                bottomAxis =
                    HorizontalAxis.rememberBottom(
                        guideline = null,
                        valueFormatter = ChartDefaults.emojiValueFormatter,
                    ),
            ),
        modelProducer = modelProducer,
        modifier = modifier,
    )
}

@Composable
fun BarChart(
    stats: MonthlyChartData?,
    modifier: Modifier = Modifier,
) {
    if (stats == null || stats.totalTilCount == 0) {
        Box(
            modifier.fillMaxWidth().height(150.dp),
            contentAlignment = Alignment.Center,
        ) { Text("데이터가 없습니다.") }
    } else {
        val modelProducer = remember { CartesianChartModelProducer() }
        LaunchedEffect(stats) {
            modelProducer.runTransaction {
                columnSeries {
                    series(
                        x = Emotion.entries.reversed().map { it.score },
                        y =
                            Emotion.entries.reversed().map {
                                stats.emotionCounts[it]?.toFloat() ?: 0f
                            },
                    )
                }
            }
        }
        BarChart(
            modelProducer,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BarChartPreview() {
    val modelProducer = remember { CartesianChartModelProducer() }

    runBlocking {
        modelProducer.runTransaction {
            columnSeries {
                series(
                    x = Emotion.entries.map { it.ordinal + 1 },
                    y =
                        Emotion.entries.map {
                            dummyStats.emotionCounts[it]?.toFloat() ?: 0f
                        },
                )
            }
        }
    }
    DalmuriTheme {
        BarChart(
            modelProducer,
        )
    }
}
