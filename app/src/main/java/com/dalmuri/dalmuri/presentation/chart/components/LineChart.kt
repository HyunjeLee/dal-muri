package com.dalmuri.dalmuri.presentation.chart.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dalmuri.dalmuri.domain.model.MonthlyChartData
import com.dalmuri.dalmuri.presentation.chart.dummy.dummyStats
import com.dalmuri.dalmuri.presentation.chart.utils.ChartDefaults
import com.dalmuri.dalmuri.presentation.theme.DalmuriTheme
import com.dalmuri.dalmuri.presentation.utils.toDayOfMonth
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.common.Fill
import com.patrykandpatrick.vico.core.common.component.LineComponent
import kotlinx.coroutines.runBlocking

@Composable
private fun LineChart(
    modelProducer: CartesianChartModelProducer,
    rangeProvider: CartesianLayerRangeProvider,
    modifier: Modifier = Modifier,
) {
    CartesianChartHost(
        chart =
            rememberCartesianChart(
                rememberLineCartesianLayer(
                    lineProvider =
                        LineCartesianLayer.LineProvider.series(
                            LineCartesianLayer.Line(
                                fill = LineCartesianLayer.LineFill.single(Fill(color = MaterialTheme.colorScheme.tertiary.toArgb())),
                                pointProvider =
                                    LineCartesianLayer.PointProvider.single(
                                        LineCartesianLayer.Point(
                                            component =
                                                LineComponent(
                                                    fill = Fill(color = MaterialTheme.colorScheme.tertiary.toArgb()),
                                                ),
                                            sizeDp = 5f,
                                        ),
                                    ),
                            ),
                        ),
                    rangeProvider = rangeProvider,
                ),
                startAxis =
                    VerticalAxis.rememberStart(
                        guideline = null,
                        valueFormatter = ChartDefaults.emojiValueFormatter,
                        itemPlacer = VerticalAxis.ItemPlacer.step({ 1.0 }),
                    ),
                bottomAxis =
                    HorizontalAxis.rememberBottom(
                        guideline = null,
                        valueFormatter = ChartDefaults.dateValueFormatter,
                        itemPlacer =
                            HorizontalAxis.ItemPlacer.aligned(
                                spacing = { 1 },
                            ),
                    ),
            ),
        modelProducer = modelProducer,
        modifier = modifier,
    )
}

@Composable
fun LineChart(
    stats: MonthlyChartData?,
    modifier: Modifier = Modifier,
) {
    if (stats == null || stats.dailyEmotionScores.isEmpty()) {
        Box(
            modifier
                .fillMaxWidth()
                .height(150.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text("데이터가 없습니다.")
        }
    } else {
        val modelProducer = remember { CartesianChartModelProducer() }

        LaunchedEffect(stats) {
            val days = stats.dailyEmotionScores.map { it.timestamp.toDayOfMonth().toInt() }
            val scores = stats.dailyEmotionScores.map { it.emotion.score }

            modelProducer.runTransaction {
                lineSeries { series(days, scores) }
            }
        }
        LineChart(
            modelProducer = modelProducer,
            rangeProvider =
                CartesianLayerRangeProvider.fixed(
                    minX = 1.0,
                    maxX = stats.yearMonth.lengthOfMonth().toDouble(),
                    minY = 0.0,
                    maxY = 5.0,
                ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LineChartPreview() {
    val modelProducer = remember { CartesianChartModelProducer() }
    runBlocking {
        modelProducer.runTransaction {
            lineSeries { series(dummyStats.dailyEmotionScores.map { it.emotion.score.toFloat() }) }
        }
    }

    DalmuriTheme { LineChart(modelProducer, CartesianLayerRangeProvider.fixed()) }
}
