package com.github.hummel.dmma.lab7.grammar

import java.lang.Double.max
import java.lang.Double.min
import kotlin.math.abs

data class Point(var x: Double, var y: Double) {
	fun move(deltaX: Double, deltaY: Double) {
		x += deltaX
		y += deltaY
	}

	companion object {
		val NONE: Point = Point(0.0, 0.0)
	}
}

data class Line(var start: Point, var end: Point) {
	fun move(deltaX: Double, deltaY: Double) {
		start.move(deltaX, deltaY)
		end.move(deltaX, deltaY)
	}

	fun resize(xScale: Double, yScale: Double, centralPoint: Point) {
		val xDelta = (end.x - start.x) * xScale
		val yDelta = (end.y - start.y) * yScale

		val xStartDelta = (start.x - centralPoint.x) * xScale
		val yStartDelta = (start.y - centralPoint.y) * yScale

		start = Point(centralPoint.y + xStartDelta, centralPoint.y + yStartDelta)
		end = Point(start.x + xDelta, start.y + yDelta)
	}
}

data class Element(val elementType: ElementType) {
	val lines: ArrayList<Line> = arrayListOf()
	var startPosition: Point = Point.NONE
	var endPosition: Point = Point.NONE

	fun length(): Double = abs(endPosition.x - startPosition.x)

	constructor(elementType: ElementType, line: Line) : this(elementType) {
		with(line) {
			lines.add(this)
			startPosition = Point(min(start.x, end.x), max(start.y, end.y))
			endPosition = Point(max(start.x, end.x), min(start.y, end.y))
		}
	}

	constructor(
		elementType: ElementType, lines: List<Line>, startPosition: Point, endPosition: Point
	) : this(elementType) {
		this.lines.addAll(lines)
		this.startPosition = startPosition
		this.endPosition = endPosition
	}

	fun move(xDelta: Double, yDelta: Double) {
		startPosition.move(xDelta, yDelta)
		endPosition.move(xDelta, yDelta)
		lines.forEach { it.move(xDelta, yDelta) }
	}

	fun resize(xScale: Double, yScale: Double) {
		val deltaX = (endPosition.x - startPosition.x) * xScale
		val deltaY = (endPosition.y - startPosition.y) * yScale

		endPosition = Point(startPosition.x + deltaX, startPosition.y + deltaY)
		lines.forEach { it.resize(xScale, yScale, startPosition) }
	}

	infix fun isSameTypeWith(compared: ElementType): Boolean = elementType.isSame(compared)
}