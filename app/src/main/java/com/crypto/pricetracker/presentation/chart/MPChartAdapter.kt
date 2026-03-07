package com.crypto.pricetracker.presentation.chart

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.crypto.domain.model.Kline
import com.crypto.pricetracker.R
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate

@Composable
fun LineChartView(
    klines: List<Kline>,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = true
                setScaleEnabled(true)
                setDragEnabled(true)
                setPinchZoom(true)
                setDoubleTapToZoomEnabled(true)

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                }

                axisLeft.apply {
                    setDrawGridLines(true)
                }

                axisRight.isEnabled = false
            }
        },
        update = { chart ->
            val context = chart.context
            val entries = klines.mapIndexed { i, kline ->
                Entry(i.toFloat(), kline.closePrice.toFloat())
            }

            val lineDataSet = LineDataSet(entries, context.getString(R.string.close_price)).apply {
                color = ColorTemplate.getHoloBlue()
                setCircleColor(ColorTemplate.getHoloBlue())
                lineWidth = 2f
                circleRadius = 3f
                setDrawCircleHole(false)
                mode = LineDataSet.Mode.LINEAR
                setDrawValues(false)
            }

            chart.data = LineData(lineDataSet)
            chart.invalidate()
        }
    )
}

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
                legend.isEnabled = true
                setScaleEnabled(true)
                setDragEnabled(true)
                setPinchZoom(true)
                isDoubleTapToZoomEnabled = true

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                }

                axisLeft.apply {
                    setDrawGridLines(true)
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
                shadowColor = Color.DKGRAY
                shadowWidth = 0.7f
                decreasingColor = Color.RED
                decreasingPaintStyle = Paint.Style.FILL
                increasingColor = Color.GREEN
                increasingPaintStyle = Paint.Style.FILL
                neutralColor = Color.GRAY
                setDrawValues(false)
            }

            chart.data = CandleData(candleDataSet)
            chart.invalidate()
        }
    )
}
