package com.github.hummel.dmma.lab7

import com.github.hummel.dmma.lab7.grammar.Element
import com.github.hummel.dmma.lab7.grammar.Line
import com.github.hummel.dmma.lab7.grammar.Point
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color

class Drawer(private val canvas: Canvas) {
	private val xStart: Double = 50.0
	private val yStart: Double = canvas.height - 200

	fun cleanCanvas() {
		canvas.graphicsContext2D.fill = Color.WHITE
		canvas.graphicsContext2D.fillRect(0.0, 0.0, canvas.width, canvas.height)
	}

	fun draw(element: Element) {
		canvas.graphicsContext2D.stroke = Color.BLACK

		val lines = element.lines
		lines.forEach { (start, end) ->
			val x1 = getXCanvasCoordinate(start.x)
			val x2 = getXCanvasCoordinate(end.x)
			val y1 = getYCanvasCoordinate(start.y)
			val y2 = getYCanvasCoordinate(end.y)

			canvas.graphicsContext2D.strokeLine(x1, y1, x2, y2)
		}
	}

	fun drawLine(from: Point, to: Point) {
		canvas.graphicsContext2D.stroke = Color.BLACK
		canvas.graphicsContext2D.strokeLine(from.x, from.y, to.x, to.y)
	}

	fun drawLine(line: Line): Unit = drawLine(line.start, line.end)

	fun getFactPoint(canvasPoint: Point): Point {
		val factX = getXFactCoordinate(canvasPoint.x)
		val factY = getYFactCoordinate(canvasPoint.y)
		return Point(factX, factY)
	}

	private fun getYCanvasCoordinate(y: Double) = yStart - y * SCALE

	private fun getXCanvasCoordinate(x: Double) = x * SCALE + xStart

	private fun getXFactCoordinate(x: Double) = (x - xStart) / SCALE

	private fun getYFactCoordinate(y: Double) = (yStart - y) / SCALE

	companion object {
		private const val SCALE = 10
	}
}
