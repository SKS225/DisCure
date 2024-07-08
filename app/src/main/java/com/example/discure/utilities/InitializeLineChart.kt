package com.example.discure.utilities

import android.graphics.Color
import com.example.discure.MyXAxisValueFormatter
import com.example.discure.models.HealthData
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate

fun setupChart(chart: LineChart) {

    chart.description.isEnabled = false
    chart.setTouchEnabled(true)
    chart.dragDecelerationFrictionCoef = 0.9f
    chart.isDragEnabled = true
    chart.setScaleEnabled(false)
    chart.setDrawGridBackground(false)
    chart.isHighlightPerTapEnabled = false
    chart.isHighlightPerDragEnabled = false
    chart.animateX(500)
    chart.isAutoScaleMinMaxEnabled = !chart.isAutoScaleMinMaxEnabled
    chart.notifyDataSetChanged()
    chart.setBackgroundColor(Color.WHITE)
    chart.setViewPortOffsets(0f, 0f, 0f, 0f)

    val l = chart.legend
    l.isEnabled = false

    val xAxis = chart.xAxis
    xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
    xAxis.textSize = 12f
    xAxis.textColor = Color.WHITE
    xAxis.setDrawAxisLine(true)
    xAxis.setDrawGridLines(false)
    xAxis.textColor = Color.rgb(0, 0, 0)
    xAxis.setCenterAxisLabels(true)
    xAxis.granularity = 1f // adjust granularity as needed
    xAxis.valueFormatter = MyXAxisValueFormatter()

    val leftAxis = chart.axisLeft
    leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
    leftAxis.textColor = ColorTemplate.getHoloBlue()
    leftAxis.setDrawGridLines(true)
    leftAxis.isGranularityEnabled = true
    leftAxis.axisMinimum = 30f
    leftAxis.axisMaximum = 120f
    leftAxis.yOffset = -9f
    leftAxis.textColor = Color.rgb(0, 0, 0)

    val rightAxis = chart.axisRight
    rightAxis.isEnabled = false
}

fun displayLineChart(chart: LineChart, healthDataList: List<HealthData>) {

    val values = ArrayList<Entry>()

    // Assuming healthDataList is sorted by date already, otherwise you may need to sort it
    for ((index, healthData) in healthDataList.withIndex()) {
        val value = healthData.value?.toFloatOrNull() ?: continue
        values.add(Entry(index.toFloat(), value))
    }

    val set1 = LineDataSet(values, "DataSet 1")
    set1.axisDependency = YAxis.AxisDependency.LEFT
    set1.color = ColorTemplate.getHoloBlue()
    set1.valueTextColor = ColorTemplate.getHoloBlue()
    set1.lineWidth = 1.5f
    set1.setDrawCircles(true)
    set1.setDrawValues(false)
    set1.fillAlpha = 65
    set1.fillColor = ColorTemplate.getHoloBlue()
    set1.highLightColor = Color.rgb(244, 117, 117)
    set1.setDrawCircleHole(false)

    val dataSets = ArrayList<ILineDataSet>()
    dataSets.add(set1)

    val data = LineData(dataSets)
    data.setValueTextColor(Color.WHITE)
    data.setValueTextSize(9f)

    chart.data = data
    chart.invalidate()
}