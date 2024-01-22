package hummel.grammar

@FunctionalInterface
interface Type {
	fun isTerminal(): Boolean
}

open class ElementType(val name: String) : Type {
	companion object {
		val NONE: ElementType = ElementType("NONE")
	}

	override fun isTerminal(): Boolean = false
}

class TerminalElementType(name: String, private val standardLine: Line) : ElementType(name) {
	override fun isTerminal(): Boolean = true

	val standardElement: Element
		get() = Element(
			this, Line(
				Point(standardLine.from.x, standardLine.from.y), Point(standardLine.to.x, standardLine.to.y)
			)
		)
}