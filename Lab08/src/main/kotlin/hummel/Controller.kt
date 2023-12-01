package hummel

import hummel.grammar.*
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent

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

	fun onCanvasMouseClicked(mouseEvent: MouseEvent) {
		from = from?.let {
			val to = Point(mouseEvent.x, mouseEvent.y)
			drawer.drawLine(it, to)
			drawnLines.add(Line(it, to))

			val factFrom = drawer.getFactPoint(it)
			val factTo = drawer.getFactPoint(to)
			val line = Line(factFrom, factTo)
			val drawnElement = Grammar.getTerminalElement(line)
			drawnElements.add(drawnElement)

			null
		} ?: Point(mouseEvent.x, mouseEvent.y)
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

	fun onCanvasMouseMove(mouseEvent: MouseEvent) {
		from?.let {
			drawer.cleanCanvas()
			drawnLines.forEach { l -> drawer.drawLine(l) }
			drawer.drawLine(it, Point(mouseEvent.x, mouseEvent.y))
		}
	}
}
