package com.dalmuri.dalmuri.presentation.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dalmuri.dalmuri.presentation.theme.DalmuriTheme

@Composable
fun CreateWrapUpScreen(onFinish: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = "Wrap up today")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onFinish) { Text(text = "Finish") }
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateWrapUpScreenPreview() {
    DalmuriTheme { CreateWrapUpScreen(onFinish = {}) }
}
