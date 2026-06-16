package com.crypto.pricetracker.presentation.detail.chart

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.crypto.core.theme.BinanceColors
import com.crypto.domain.model.Kline
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry

@Composable
fun CandleStickChartView(
    klines: List<Kline>,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            CandleStickChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = false
                setScaleEnabled(true)
                setDragEnabled(true)
                setPinchZoom(true)
                isDoubleTapToZoomEnabled = true
                setBackgroundColor(BinanceColors.BACKGROUND_PRIMARY.toInt())
                setDrawGridBackground(false)

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    textColor = BinanceColors.TEXT_SECONDARY.toInt()
                    axisLineColor = BinanceColors.SURFACE.toInt()
                }
                axisLeft.apply {
                    setDrawGridLines(true)
                    gridColor = BinanceColors.SURFACE.toInt()
                    textColor = BinanceColors.TEXT_SECONDARY.toInt()
                    axisLineColor = BinanceColors.SURFACE.toInt()
                }
                axisRight.isEnabled = false
            }
        },
        update = { chart ->
            val candleEntries = klines.mapIndexed { i, kline ->
                CandleEntry(
                    i.toFloat(),
                    kline.highPrice.toFloat(),
                    kline.lowPrice.toFloat(),
                    kline.openPrice.toFloat(),
                    kline.closePrice.toFloat()
                )
            }
            val symbol = klines.firstOrNull()?.symbol ?: ""
            val candleDataSet = CandleDataSet(candleEntries, symbol).apply {
                shadowColor = Color.GRAY
                shadowWidth = 0.8f
                decreasingColor = BinanceColors.RED_DOWN.toInt()
                decreasingPaintStyle = Paint.Style.FILL
                increasingColor = BinanceColors.GREEN_UP.toInt()
                increasingPaintStyle = Paint.Style.FILL
                neutralColor = BinanceColors.TEXT_SECONDARY.toInt()
                setDrawValues(false)
            }
            chart.data = CandleData(candleDataSet)
            chart.invalidate()
        }
    )
}
