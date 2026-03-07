package com.crypto.pricetracker.presentation.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crypto.domain.model.Kline
import com.crypto.pricetracker.R

private const val DEFAULT_CHART_HEIGHT = 450

enum class ChartMode {
    LINE, CANDLESTICK
}

@Composable
fun KlineChart(
    klines: List<Kline>,
    modifier: Modifier = Modifier,
    chartHeight: Int = DEFAULT_CHART_HEIGHT
) {
    var chartMode by remember { mutableStateOf(ChartMode.LINE) }

    if (klines.isEmpty()) {
        Text(
            text = stringResource(R.string.no_klines_data_available),
            modifier = modifier
        )
    } else {
        Column(modifier = modifier.fillMaxWidth()) {
            // Chart mode selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color(0xFFF5F5F5))
                    .padding(8.dp)
            ) {
                ChartMode.entries.forEach { mode ->
                    val isSelected = chartMode == mode
                    Text(
                        text = if (mode == ChartMode.LINE) {
                            stringResource(R.string.line_chart)
                        } else {
                            stringResource(R.string.candlestick)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                if (isSelected) Color(0xFF1976D2) else Color.White,
                                shape = MaterialTheme.shapes.small
                            )
                            .clickable {
                                chartMode = mode
                            }
                            .padding(8.dp),
                        color = if (isSelected) Color.White else Color.Black,
                        fontSize = 14.sp
                    )
                }
            }

            // Chart view based on mode
            when (chartMode) {
                ChartMode.LINE -> {
                    LineChartView(
                        klines = klines,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(chartHeight.dp)
                    )
                }
                ChartMode.CANDLESTICK -> {
                    CandleStickChartView(
                        klines = klines,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(chartHeight.dp)
                    )
                }
            }
        }
    }
}
