package hummel.grammar

import kotlin.math.abs

//terminals
private const val HORIZONTAL_LINE = "horizontal"
private const val VERTICAL_LINE = "vertical"
private const val SLASH_LINE = "slope"
private const val BACK_SLASH_LINE = "back_slope"

//non-terminals
private const val LEFT_BRANCH = "l_branch"
private const val RIGHT_BRANCH = "r_branch"
private const val BRANCH_LAYER = "layer"
private const val TREE = "tree"
private const val BIG_TREE = "big_tree"

class ElementGenerator {
	private val dictionary: Map<String, ElementType>
	private val rules: Map<String, Rule>
	private val startElementType: ElementType

	init {
		dictionary = createDictionary()
		rules = createRules()
		startElementType = ElementType(BIG_TREE)
	}

	private fun createDictionary() = mapOf(
		HORIZONTAL_LINE to TerminalElementType(HORIZONTAL_LINE, Line(Point(.0, .0), Point(10.0, .0))),
		VERTICAL_LINE to TerminalElementType(VERTICAL_LINE, Line(Point(.0, .0), Point(.0, 10.0))),
		SLASH_LINE to TerminalElementType(SLASH_LINE, Line(Point(.0, .0), Point(10.0, 10.0))),
		BACK_SLASH_LINE to TerminalElementType(BACK_SLASH_LINE, Line(Point(10.0, .0), Point(.0, 10.0))),

		LEFT_BRANCH to ElementType(LEFT_BRANCH),
		RIGHT_BRANCH to ElementType(RIGHT_BRANCH),
		BRANCH_LAYER to ElementType(BRANCH_LAYER),
		TREE to ElementType(TREE),
		BIG_TREE to ElementType(BIG_TREE)
	)

	private fun createRules() = mapOf(
		LEFT_BRANCH to LeftRule(dictionary[LEFT_BRANCH]!!, dictionary[SLASH_LINE]!!, dictionary[VERTICAL_LINE]!!),
		RIGHT_BRANCH to LeftRule(
			dictionary[RIGHT_BRANCH]!!, dictionary[VERTICAL_LINE]!!, dictionary[BACK_SLASH_LINE]!!
		),
		BRANCH_LAYER to LeftRule(dictionary[BRANCH_LAYER]!!, dictionary[LEFT_BRANCH]!!, dictionary[RIGHT_BRANCH]!!),
		TREE to UpRule(dictionary[TREE]!!, dictionary[BRANCH_LAYER]!!, dictionary[BRANCH_LAYER]!!),
		BIG_TREE to UpRule(dictionary[BIG_TREE]!!, dictionary[TREE]!!, dictionary[BRANCH_LAYER]!!)
	)

	fun getTerminalElement(line: Line): Element {
		val elementName = getTerminalElementName(line)
		return Element(dictionary[elementName]!!, line)
	}

	private fun getTerminalElementName(line: Line): String {
		val deltaX = line.start.x - line.end.x
		val deltaY = line.start.y - line.end.y
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
		val highPoint = if (line.end.y > line.start.y) line.end else line.start
		val lowPoint = if (line.end.y < line.start.y) line.end else line.start
		if (highPoint.x < lowPoint.x) {
			return BACK_SLASH_LINE
		}
		return SLASH_LINE
	}

	fun generateElement(elementType: ElementType = startElementType): Element {
		if (elementType.isTerminal()) {
			return (elementType as TerminalElementType).getStandardElement()
		}

		val rule = rules[elementType.name]!!
		return rule.transformConnect(
			generateElement(rule.firstElementType), generateElement(rule.secondElementType)
		)
	}

	fun isImageCorrect(elements: List<Element>): Boolean {
		val correctLines = generateElement().lines
		val correctElements = correctLines.map { getTerminalElement(it) }

		if (correctElements.size != elements.size) {
			return false
		}

		return correctElements.indices.any { elements[it] isSameTypeWith correctElements[it].elementType }
	}
}