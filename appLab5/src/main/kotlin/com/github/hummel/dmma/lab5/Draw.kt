package com.github.hummel.dmma.lab5

import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartUtils
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import java.awt.BasicStroke
import java.io.File

fun draw(points: List<ClassifiedPoint>, xRange: Range, yRange: Range, chartFunction: ChartFunction, index: Int) {
	val dataset = XYSeriesCollection()
	val resourcePath = "appLab5"

	val series1 = XYSeries("Class 1 points")
	val series2 = XYSeries("Class 2 points")
	val series3 = XYSeries("Separation function")

	points.forEach { (x, y, classIndex) -> (if (classIndex == 1) series1 else series2).add(x, y) }

	xRange.forEachWithStep(0.01) { series3.add(it, chartFunction(it)) }

	dataset.addSeries(series1)
	dataset.addSeries(series2)
	dataset.addSeries(series3)

	val chart = ChartFactory.createXYLineChart(
		"Potential Classifier", "X", "Y", dataset, PlotOrientation.VERTICAL, true, false, false
	)

	val plot = chart.plot as XYPlot
	plot.domainAxis.setRange(xRange.from, xRange.to)
	plot.rangeAxis.setRange(yRange.from, yRange.to)

	val renderer = plot.renderer as XYLineAndShapeRenderer
	renderer.setSeriesLinesVisible(0, false)
	renderer.setSeriesShapesVisible(0, true)
	renderer.setSeriesLinesVisible(1, false)
	renderer.setSeriesShapesVisible(1, true)
	renderer.setSeriesStroke(2, BasicStroke(2F))

	ChartUtils.saveChartAsPNG(File("$resourcePath/images/classification-chart-$index.jpg"), chart, 1000, 1000)
}