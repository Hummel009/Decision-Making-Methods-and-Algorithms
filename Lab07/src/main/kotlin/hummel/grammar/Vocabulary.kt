package hummel.grammar

interface Type {
	fun isTerminal(): Boolean
}

open class ElementType(val name: String) : Type {
	override fun isTerminal(): Boolean = false

	fun isSame(compared: ElementType): Boolean = this.name == compared.name
}

class TerminalElementType(name: String, private val standardLine: Line) : ElementType(name) {
	override fun isTerminal(): Boolean = true

	fun getStandardElement(): Element {
		val lineCopy = Line(
			Point(standardLine.start.x, standardLine.start.y), Point(standardLine.end.x, standardLine.end.y)
		)
		return Element(this, lineCopy)
	}
}