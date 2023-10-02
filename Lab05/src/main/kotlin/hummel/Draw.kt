package hummel

import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartUtils
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import java.awt.BasicStroke
import java.io.File

// Функция для отрисовки графика распознавания объектов методом потенциалов
fun draw(points: List<Point>, xRange: Range, yRange: Range, chartFunction: ChartFunction, index: Int) {
	// Создание коллекции данных для графика
	val dataset = XYSeriesCollection()

	// Создание серий данных для классов и функции разделения
	val series1 = XYSeries("Class 1 points")
	val series2 = XYSeries("Class 2 points")
	val series3 = XYSeries("Separation function")

	// Заполнение серий данными точек классов
	points.forEach { (x, y, classIndex) ->
		if (classIndex == 1) {
			series1
		} else {
			series2
		}.add(x, y)
	}

	// Заполнение серии данных для функции разделения
	xRange.forEachWithStep(0.01) {
		series3.add(it, chartFunction(it))
	}

	// Добавление серий в коллекцию данных
	dataset.addSeries(series1)
	dataset.addSeries(series2)
	dataset.addSeries(series3)

	// Создание графика с использованием библиотеки JFreeChart
	val chart = ChartFactory.createXYLineChart(
		"Potential Classifier", "X", "Y", dataset, PlotOrientation.VERTICAL, true, false, false
	)

	// Получение объекта XYPlot для настройки осей
	val plot: XYPlot = chart.plot as XYPlot
	plot.domainAxis.setRange(xRange.from, xRange.to)
	plot.rangeAxis.setRange(yRange.from, yRange.to)

	// Получение объекта рендерера для настройки внешнего вида графика
	val renderer = plot.renderer as XYLineAndShapeRenderer
	renderer.setSeriesLinesVisible(0, false)
	renderer.setSeriesShapesVisible(0, true)
	renderer.setSeriesLinesVisible(1, false)
	renderer.setSeriesShapesVisible(1, true)
	renderer.setSeriesStroke(2, BasicStroke(2F))

	// Сохранение графика в файл формата PNG
	ChartUtils.saveChartAsPNG(File("images/classification-chart-$index.jpg"), chart, 1000, 1000)
}