package hummel

import hummel.grammar.Element
import hummel.grammar.ElementGenerator
import hummel.grammar.Line
import hummel.grammar.Point
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent

class Controller {
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
		from = from?.let {
			val to = Point(mouseEvent.x, mouseEvent.y)
			drawer.drawLine(it, to)
			drawedLines.add(Line(it, to))

			val factFrom = drawer.getFactPoint(it)
			val factTo = drawer.getFactPoint(to)
			val line = Line(factFrom, factTo)
			val drewElement = generator.getTerminalElement(line)
			drawedElements.add(drewElement)

			null
		} ?: Point(mouseEvent.x, mouseEvent.y)
	}

	fun onCanvasMouseMove(mouseEvent: MouseEvent) {
		from?.let {
			drawer.cleanCanvas()
			drawedLines.forEach { l -> drawer.drawLine(l) }
			drawer.drawLine(it, Point(mouseEvent.x, mouseEvent.y))
		}
	}
}

