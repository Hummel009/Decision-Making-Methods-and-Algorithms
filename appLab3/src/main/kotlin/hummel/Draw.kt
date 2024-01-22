package hummel

import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartUtils
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.title.TextTitle
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import java.io.File
import java.util.*

// Функция для отрисовки графика и сохранения его в виде изображения.
fun draw(xValues: DoubleArray, y1Values: DoubleArray, y2Values: DoubleArray, areas: Pair<Double, Double>) {
	// Создание набора данных для графика.
	val resourcePath = "appLab3"
	val dataset = XYSeriesCollection()
	val series1 = XYSeries("First Function")
	val series2 = XYSeries("Second Function")

	// Заполнение данных для первой и второй функции.
	for (i in xValues.indices) {
		series1.add(xValues[i], y1Values[i])
		series2.add(xValues[i], y2Values[i])
	}

	dataset.addSeries(series1)
	dataset.addSeries(series2)

	// Создание графика с заданными параметрами.
	val chart = ChartFactory.createXYAreaChart(
		"Probabilistic Classification", "X", "Y", dataset, PlotOrientation.VERTICAL, true, false, false
	)

	// Добавление подзаголовков с информацией о доле ошибок.
	chart.addSubtitle(TextTitle("Detection mistake: ${String.format("%.3f", areas.first * 100)}%"))
	chart.addSubtitle(TextTitle("False positive: ${String.format("%.3f", areas.second * 100)}%"))
	chart.addSubtitle(TextTitle("Summary mistake: ${String.format("%.3f", (areas.first + areas.second) * 100)}%"))

	// Сохранение графика как изображения PNG.
	ChartUtils.saveChartAsPNG(
		File("$resourcePath/images/clustering-mistake-${String.format(Locale.ROOT, "%.1f", PROBABILITY_1)}.jpg"),
		chart,
		800,
		500
	)
}