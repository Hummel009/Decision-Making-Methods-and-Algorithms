package com.github.hummel.dmma.lab8

import com.github.hummel.dmma.lab8.grammar.*
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent

@Suppress("MemberVisibilityCanBePrivate")
class Controller {
	@FXML
	private lateinit var grammarLabel: Label

	@FXML
	private lateinit var canvas: Canvas

	@FXML
	private lateinit var resultLabel: Label

	private lateinit var drawer: Drawer
	private var grammar: Grammar? = null

	private var from: Point? = null
	private lateinit var drawnElements: MutableList<Element>
	private lateinit var drawnLines: MutableList<Line>

	fun initialize() {
		drawer = Drawer(canvas)
		drawnElements = ArrayList()
		drawnLines = ArrayList()
		clean()
	}

	fun generate() {
		drawer.cleanCanvas()
		grammar?.let { g ->
			val element = g.generateElement()
			drawer.draw(element)

			drawnElements.clear()
			drawnLines.clear()
			element.lines.forEach {
				val terminal = Grammar.getTerminalElement(it)
				drawnElements.add(terminal)
				drawnLines.add(it)
			}
		} ?: run {
			resultLabel.text = "No grammar."
		}
	}

	fun clean() {
		drawer.cleanCanvas()
		resultLabel.text = ""
		grammarLabel.text = ""
		grammar = null
		drawnElements.clear()
		drawnLines.clear()
	}

	fun synthesizeGrammar() {
		val listCopy = drawnElements.map { it }
		try {
			val generator = GrammarGenerator(listCopy.reversed().toMutableList())
			grammar = generator.grammar
			grammarLabel.text = generator.grammar.toString()
		} catch (e: InvalidElementException) {
			resultLabel.text = "$e"
			grammarLabel.text = ""
		}
	}

	fun onCanvasMouseClicked(mouseEvent: MouseEvent) {
		from = if (from == null) {
			Point(mouseEvent.x, mouseEvent.y)
		} else {
			val to = Point(mouseEvent.x, mouseEvent.y)
			drawer.drawLine(from ?: return, to)
			drawnLines.add(Line(from ?: return, to))

			val factFrom = drawer.getFactPoint(from ?: return)
			val factTo = drawer.getFactPoint(to)
			val line = Line(factFrom, factTo)
			val drawnElement = Grammar.getTerminalElement(line)
			drawnElements.add(drawnElement)

			null
		}
	}

	fun onCanvasMouseMove(mouseEvent: MouseEvent) {
		if (from != null) {
			drawer.cleanCanvas()
			for (line in drawnLines) {
				drawer.drawLine(line)
			}
			drawer.drawLine(from ?: return, Point(mouseEvent.x, mouseEvent.y))
		}
	}
}
