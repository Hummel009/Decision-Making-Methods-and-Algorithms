package com.github.hummel.dmma.lab8.grammar

class InvalidElementException : RuntimeException() {
	override fun toString(): String = "Can't create grammar"
}