package com.dalmuri.dalmuri.presentation.create.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp

@Composable
fun CreateInputArea(
    text: String,
    onValueChange: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = false,
) {
    var isFocused by remember { mutableStateOf(false) }

    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .then(
                    if (singleLine) {
                        Modifier.wrapContentHeight()
                    } else {
                        Modifier.heightIn(min = 150.dp)
                    },
                ),
        shape = RoundedCornerShape(16.dp),
    ) {
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            textStyle =
                MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                ),
            singleLine = singleLine,
            modifier =
                Modifier
                    .then(
                        if (singleLine) {
                            Modifier.fillMaxWidth()
                        } else {
                            Modifier.fillMaxSize()
                        },
                    ).onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    }.padding(16.dp),
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.TopStart) {
                    if (text.isEmpty() && !isFocused) {
                        Text(
                            text = hint,
                            style =
                                MaterialTheme.typography.bodyMedium.copy(
                                    color =
                                        MaterialTheme.colorScheme.onSurfaceVariant,
                                ),
                        )
                    }
                    innerTextField()
                }
            },
        )
    }
}
