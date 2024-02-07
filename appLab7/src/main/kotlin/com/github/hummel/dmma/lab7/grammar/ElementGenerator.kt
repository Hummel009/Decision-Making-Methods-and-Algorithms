package com.github.hummel.dmma.lab7.grammar

import kotlin.math.abs

//terminals
private const val HORIZONTAL_LINE = "horizontal"
private const val VERTICAL_LINE = "vertical"
private const val RIGHT_45_DEG = "right45"
private const val LEFT_45_DEG = "left45"

//non-terminals
private const val LEFT_BRANCH = "l_branch"
private const val RIGHT_BRANCH = "r_branch"
private const val BRANCH_LAYER = "layer"
private const val TREE = "tree"
private const val START_S = "big_tree"

class ElementGenerator {
	private val dictionary: Map<String, ElementType>
	private val rules: Map<String, Rule>
	private val startElementType: ElementType

	init {
		dictionary = createDictionary()
		rules = createRules()
		startElementType = ElementType(START_S)
	}

	private fun createDictionary() = mapOf(
		HORIZONTAL_LINE to TerminalElementType(
			HORIZONTAL_LINE, Line(Point(.0, .0), Point(10.0, .0))
		),
		VERTICAL_LINE to TerminalElementType(
			VERTICAL_LINE, Line(Point(.0, .0), Point(.0, 10.0))
		),
		RIGHT_45_DEG to TerminalElementType(
			RIGHT_45_DEG, Line(Point(.0, .0), Point(10.0, 10.0))
		),
		LEFT_45_DEG to TerminalElementType(
			LEFT_45_DEG, Line(Point(10.0, .0), Point(.0, 10.0))
		),

		LEFT_BRANCH to ElementType(LEFT_BRANCH),
		RIGHT_BRANCH to ElementType(RIGHT_BRANCH),
		BRANCH_LAYER to ElementType(BRANCH_LAYER),
		TREE to ElementType(TREE),
		START_S to ElementType(START_S)
	)

	private fun createRules() = mapOf(
		LEFT_BRANCH to LeftRule(
			dictionary.getValue(LEFT_BRANCH), dictionary.getValue(RIGHT_45_DEG), dictionary.getValue(VERTICAL_LINE)
		), RIGHT_BRANCH to LeftRule(
			dictionary.getValue(RIGHT_BRANCH), dictionary.getValue(VERTICAL_LINE), dictionary.getValue(LEFT_45_DEG)
		), BRANCH_LAYER to LeftRule(
			dictionary.getValue(BRANCH_LAYER), dictionary.getValue(LEFT_BRANCH), dictionary.getValue(RIGHT_BRANCH)
		), TREE to UpRule(
			dictionary.getValue(TREE), dictionary.getValue(BRANCH_LAYER), dictionary.getValue(BRANCH_LAYER)
		), START_S to UpRule(
			dictionary.getValue(START_S), dictionary.getValue(TREE), dictionary.getValue(BRANCH_LAYER)
		)/*, START_S to UpRule(
			dictionary[START_S]!!, dictionary[HORIZONTAL_LINE]!!, dictionary[VERTICAL_LINE]!!
		)*/
	)

	fun getTerminalElement(line: Line): Element {
		val elementName = getTerminalElementName(line)
		return Element(dictionary.getValue(elementName), line)
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
			return LEFT_45_DEG
		}
		return RIGHT_45_DEG
	}

	fun generateElement(elementType: ElementType = startElementType): Element {
		if (elementType.isTerminal()) {
			return (elementType as TerminalElementType).getStandardElement()
		}

		val rule = rules.getValue(elementType.name)
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