package com.dalmuri.dalmuri

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.dalmuri.dalmuri.presentation.navigation.AppNavigation
import com.dalmuri.dalmuri.presentation.theme.DalmuriTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DalmuriTheme {
                AppNavigation(modifier = Modifier.fillMaxSize())
            }
        }
    }
}
