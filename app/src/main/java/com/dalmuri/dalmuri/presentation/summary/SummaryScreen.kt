package com.dalmuri.dalmuri.presentation.summary

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dalmuri.dalmuri.presentation.theme.DalmuriTheme

@Composable
fun SummaryScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) { Text(text = "Summary Screen") }
}

@Preview(showBackground = true)
@Composable
private fun SummaryScreenPreview() {
    DalmuriTheme { SummaryScreen() }
}
