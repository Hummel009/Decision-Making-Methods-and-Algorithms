package com.github.hummel.dmma.lab7

import com.github.hummel.dmma.lab7.grammar.Element
import com.github.hummel.dmma.lab7.grammar.ElementGenerator
import com.github.hummel.dmma.lab7.grammar.Line
import com.github.hummel.dmma.lab7.grammar.Point
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent

@Suppress("unused")
class Controller {
	lateinit var grammarLabel: Label

	@FXML
	private lateinit var canvas: Canvas

	@FXML
	private lateinit var resultLabel: Label

	private lateinit var drawer: Drawer
	private lateinit var generator: ElementGenerator

	private var from: Point? = null
	private var drawedElements: MutableList<Element> = ArrayList()
	private var drawedLines: MutableList<Line> = ArrayList()

	fun initialize() {
		drawer = Drawer(canvas)
		drawer.cleanCanvas()

		generator = ElementGenerator()

		resultLabel.text = ""
		resultLabel.isVisible = true
	}

	fun generate() {
		clean()
		val element = generator.generateElement()
		drawer.draw(element)

		element.lines.map { generator.getTerminalElement(it) }.forEach { drawedElements.add(it) }
	}

	fun validate() {
		if (generator.isImageCorrect(drawedElements)) {
			resultLabel.text = "Image is correct."
		} else {
			resultLabel.text = "Image is not correct."
		}
	}

	fun clean() {
		drawer.cleanCanvas()
		drawedElements.clear()
		drawedLines.clear()
		resultLabel.text = ""
	}

	fun onCanvasMouseClicked(mouseEvent: MouseEvent) {
		from = if (from == null) {
			Point(mouseEvent.x, mouseEvent.y)
		} else {
			val to = Point(mouseEvent.x, mouseEvent.y)
			drawer.drawLine(from ?: return, to)
			drawedLines.add(Line(from ?: return, to))

			val factFrom = drawer.getFactPoint(from ?: return)
			val factTo = drawer.getFactPoint(to)
			val line = Line(factFrom, factTo)
			val drewElement = generator.getTerminalElement(line)
			drawedElements.add(drewElement)

			null
		}
	}

	fun onCanvasMouseMove(mouseEvent: MouseEvent) {
		if (from != null) {
			drawer.cleanCanvas()
			for (line in drawedLines) {
				drawer.drawLine(line)
			}
			drawer.drawLine(from ?: return, Point(mouseEvent.x, mouseEvent.y))
		}
	}
}

