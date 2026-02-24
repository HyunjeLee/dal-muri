package com.dalmuri.dalmuri.presentation.chart.components

import android.R.attr.padding
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dalmuri.dalmuri.presentation.theme.DalmuriTheme

@Composable
fun AiInsightCard(
    isLoading: Boolean = true,
    summary: String? = null,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
        ) {
            when (isLoading) {
                true -> {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        contentAlignment = Alignment.Center,
                    ) { CircularProgressIndicator() }
                }
                false -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "💡 AI 인사이트", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = summary ?: "AI 분석 실패")
                }
            }
        }
    }
}

@Preview
@Composable
private fun AiInsightCardPreview() {
    DalmuriTheme { AiInsightCard() }
}
