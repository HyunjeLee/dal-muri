package com.dalmuri.dalmuri.presentation.chart.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dalmuri.dalmuri.domain.model.Emotion
import com.dalmuri.dalmuri.domain.model.MonthlyChartData
import com.dalmuri.dalmuri.presentation.chart.dummy.dummyStats
import com.dalmuri.dalmuri.presentation.theme.DalmuriTheme
import com.dalmuri.dalmuri.presentation.utils.color
import com.dalmuri.dalmuri.presentation.utils.emoji
import kotlin.math.min

@Composable
fun DonutChart(stats: MonthlyChartData?) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    MaterialTheme.colorScheme.background,
                    RoundedCornerShape(12.dp),
                ).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (stats == null || stats.totalTilCount == 0) {
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) { Text("데이터가 없습니다.") }
        } else {
            Box(modifier = Modifier.size(150.dp).weight(1f)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val diameter = min(size.width, size.height)
                    val offset =
                        Offset(
                            x = (size.width - diameter) / 2f,
                            y = (size.height - diameter) / 2f,
                        )
                    var startAngle = -90f
                    val total = stats.totalTilCount.toFloat()

                    Emotion.entries.forEach { emotion ->
                        val count = stats.emotionCounts[emotion] ?: 0
                        if (count > 0) {
                            val sweepAngle = (count / total) * 360f
                            drawArc(
                                color = emotion.color,
                                startAngle = startAngle,
                                sweepAngle = sweepAngle,
                                useCenter = false,
                                style = Stroke(width = 100f),
                                size = Size(diameter, diameter),
                                topLeft = offset,
                            )
                            startAngle += sweepAngle
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(start = 16.dp)) {
                Emotion.entries.forEach { emotion ->
                    if ((stats.emotionCounts[emotion] ?: 0) > 0) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier =
                                    Modifier
                                        .size(12.dp)
                                        .background(
                                            emotion.color,
                                            RoundedCornerShape(2.dp),
                                        ),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = emotion.emoji, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DonutChartPreview() {
    DalmuriTheme { DonutChart(dummyStats) }
}
