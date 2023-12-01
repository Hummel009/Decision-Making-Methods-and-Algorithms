package hummel.grammar

private const val RANDOM_DELTA = 3

abstract class Rule(
	val startElementType: ElementType, val firstElementType: ElementType, val secondElementType: ElementType
) {
	abstract fun connect(first: Element, second: Element): Element
	abstract fun transformConnect(first: Element, second: Element): Element
	abstract fun isRulePair(first: Element, second: Element): Boolean
}

class UpRule(
	startElementType: ElementType, firstElementType: ElementType, secondElementType: ElementType
) : Rule(startElementType, firstElementType, secondElementType) {

	override fun connect(first: Element, second: Element): Element {
		val resultLines = first.lines
		resultLines.addAll(second.lines)
		return Element(startElementType, resultLines, first.startPosition, second.endPosition)
	}

	private fun makeSameLength(first: Element, second: Element) {
		val longestElement = getLongestElement(first, second)
		val shortestElement = getShortestElement(first, second)

		shortestElement.resize(longestElement.length() / shortestElement.length(), 1.0)
	}

	override fun transformConnect(first: Element, second: Element): Element {
		makeSameLength(first, second)
		first.move(0.0, second.startPosition.y)
		return connect(first, second)
	}

	override fun isRulePair(first: Element, second: Element): Boolean =
		!(!(first isSameTypeWith firstElementType) || !(second isSameTypeWith secondElementType)) && (second.startPosition.y - RANDOM_DELTA < first.endPosition.y)

	private fun getLongestElement(first: Element, second: Element): Element =
		if (first.length() > second.length()) first else second

	private fun getShortestElement(first: Element, second: Element): Element =
		if (first.length() < second.length()) first else second
}

class LeftRule(
	startElementType: ElementType, firstElementType: ElementType, secondElementType: ElementType
) : Rule(startElementType, firstElementType, secondElementType) {
	override fun connect(first: Element, second: Element): Element {
		val resultLines = first.lines + second.lines

		val startY = first.startPosition.y.coerceAtLeast(second.startPosition.y)
		val endY = first.endPosition.y.coerceAtMost(second.endPosition.y)

		val startPosition = Point(first.startPosition.x, startY)
		val endPosition = Point(second.endPosition.x, endY)

		return Element(startElementType, resultLines, startPosition, endPosition)
	}

	override fun transformConnect(first: Element, second: Element): Element {
		second.move(first.length(), .0)
		return connect(first, second)
	}

	override fun isRulePair(first: Element, second: Element): Boolean =
		!(!(first isSameTypeWith firstElementType) || !(second isSameTypeWith secondElementType)) && (second.startPosition.y - RANDOM_DELTA < first.endPosition.y)
}