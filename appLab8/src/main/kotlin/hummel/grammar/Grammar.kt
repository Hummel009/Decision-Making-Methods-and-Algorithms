package hummel.grammar

import kotlin.math.abs

private const val HORIZONTAL_LINE = "horizontal"
private const val VERTICAL_LINE = "vertical"
private const val RIGHT_45_DEG = "right45"
private const val LEFT_45_DEG = "left45"

class Grammar {
	private val rules: MutableMap<String, Rule> = mutableMapOf()
	var startElementType: ElementType? = null

	companion object {
		private val dictionary: MutableMap<String, ElementType> = mutableMapOf(
			HORIZONTAL_LINE to TerminalElementType(HORIZONTAL_LINE, Line(Point(.0, .0), Point(10.0, .0))),
			VERTICAL_LINE to TerminalElementType(VERTICAL_LINE, Line(Point(.0, .0), Point(.0, 10.0))),
			RIGHT_45_DEG to TerminalElementType(RIGHT_45_DEG, Line(Point(.0, .0), Point(10.0, 10.0))),
			LEFT_45_DEG to TerminalElementType(LEFT_45_DEG, Line(Point(10.0, .0), Point(.0, 10.0)))
		)

		fun getTerminalElement(line: Line): Element {
			val elementName = getTerminalElementName(line)
			val elementType = dictionary.getValue(elementName)
			return Element(elementType, line)
		}

		private fun getTerminalElementName(line: Line): String {
			val deltaX = line.from.x - line.to.x
			val deltaY = line.from.y - line.to.y
			if (abs(deltaY) < 1) {
				return HORIZONTAL_LINE
			}
			if (abs(deltaX) < 1) {
				return VERTICAL_LINE
			}

			if (abs(deltaX / deltaY) < 0.2) {
				return VERTICAL_LINE
			}

			if (abs(deltaY / deltaX) < 0.2) {
				return HORIZONTAL_LINE
			}
			val highPoint = if (line.to.y > line.from.y) line.to else line.from
			val lowPoint = if (line.to.y < line.from.y) line.to else line.from
			if (highPoint.x < lowPoint.x) {
				return LEFT_45_DEG
			}
			return RIGHT_45_DEG
		}

	}

	fun generateElement(): Element = generateElement(startElementType!!)

	private fun generateElement(elementType: ElementType): Element {
		if (elementType.isTerminal()) {
			return (elementType as TerminalElementType).standardElement
		}
		val rule = rules.getValue(elementType.name)
		return rule.transformConnect(
			generateElement(rule.firstElementType), generateElement(rule.secondElementType)
		)
	}

	fun addElementType(elementType: ElementType) {
		dictionary[elementType.name] = elementType
	}

	fun addRule(rule: Rule) {
		rules[rule.startElementType.name] = rule
	}

	override fun toString(): String {
		val result = StringBuilder()
		rules.values.forEach {
			result.append(
				"${it.startElementType.name} -> " + "${it.name}(${it.firstElementType.name}, " + "${it.secondElementType.name}); "
			)
			result.append("\n")
		}
		return "$result"
	}
}