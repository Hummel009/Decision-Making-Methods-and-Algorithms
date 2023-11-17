package hummel.grammar

class InvalidElementException : RuntimeException() {
	override fun toString(): String = "Can't create grammar"
}