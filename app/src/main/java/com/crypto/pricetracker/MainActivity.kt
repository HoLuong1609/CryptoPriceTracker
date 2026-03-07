package com.crypto.pricetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.crypto.pricetracker.presentation.chart.ChartRoute
import com.crypto.pricetracker.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ChartRoute(
                    symbol = "BTCUSDT",
                    onBackClick = { finish() },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}