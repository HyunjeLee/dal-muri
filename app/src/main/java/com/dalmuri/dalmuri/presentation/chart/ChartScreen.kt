package com.dalmuri.dalmuri.presentation.chart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dalmuri.dalmuri.presentation.theme.DalmuriTheme

@Composable
fun ChartScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) { Text(text = "Summary Screen") }
}

@Preview(showBackground = true)
@Composable
private fun ChartScreenPreview() {
    DalmuriTheme { ChartScreen() }
}
