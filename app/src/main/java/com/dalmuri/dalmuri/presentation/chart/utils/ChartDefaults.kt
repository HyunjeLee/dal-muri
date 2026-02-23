package com.dalmuri.dalmuri.presentation.chart.utils

import com.dalmuri.dalmuri.domain.model.Emotion
import com.dalmuri.dalmuri.presentation.utils.emoji
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.common.data.ExtraStore

object ChartDefaults {
    // 최소 MIN_MAX를 유지하는 RangeProvider 정의
    val rangeProvider =
        object : CartesianLayerRangeProvider { // todo: 테스트 코드 작성 필요  // 10인 경우
            private val MIN_MAX = 5.0

            override fun getMaxY(
                minY: Double,
                maxY: Double,
                extraStore: ExtraStore,
            ): Double = maxOf(a = MIN_MAX, b = maxY)
        }

    // 추출한 ValueFormatter 정의
    val emojiValueFormatter =
        CartesianValueFormatter { _, value, _ ->
            if (value.toInt() == 0) return@CartesianValueFormatter " "
            Emotion.fromScore(value.toInt()).emoji
        }
    val integerValueFormatter =
        CartesianValueFormatter { _, value, _ ->
            value.toInt().toString()
        }
    val dateValueFormatter =
        CartesianValueFormatter { _, value, _ ->
            val day = value.toInt().toString()

            "${day}일"
        }
}
