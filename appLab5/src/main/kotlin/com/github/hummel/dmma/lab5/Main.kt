package com.github.hummel.dmma.lab5

import java.security.SecureRandom

private val random = SecureRandom()
val xRange: Range = Range(-5.0, 5.0)
val yRange: Range = Range(-3.0, 3.0)

val trainSetEgor: MutableList<ClassifiedPoint> = mutableListOf(
	ClassifiedPoint(-1.0, 1.0, 1),
	ClassifiedPoint(1.0, 1.0, 1),
	ClassifiedPoint(2.0, 0.0, 2),
	ClassifiedPoint(-1.0, 2.0, 2)
)

val trainSet: MutableList<ClassifiedPoint> = mutableListOf(
	ClassifiedPoint(-1.0, 0.0, 1),
	ClassifiedPoint(1.0, 1.0, 1),
	ClassifiedPoint(2.0, 0.0, 2),
	ClassifiedPoint(1.0, -2.0, 2)
)

val trainSetIntersectedLines: MutableList<ClassifiedPoint> = mutableListOf(
	ClassifiedPoint(-1.0, -1.0, 1),
	ClassifiedPoint(1.0, 1.0, 1),
	ClassifiedPoint(-1.0, 1.0, 2),
	ClassifiedPoint(1.0, -1.0, 2)
)

fun main() {
	val dataSets = arrayOf(trainSet, trainSetIntersectedLines, trainSetEgor)

	dataSets.forEachIndexed { index, dataSet ->
		//разделяющая функция и решающее правило
		val (decisionFunction, chartFunction) = generateDecisionFunction(dataSet)

		val points = List(1000) { UnclassifiedPoint(xRange.random(), yRange.random()) }

		val classifiedPoints = points.map { decisionFunction(it) }
		draw(classifiedPoints, xRange, yRange, chartFunction, index)
	}
}

data class Range(val from: Double, val to: Double) {
	fun random(): Double = from + (to - from) * random.nextDouble()

	fun forEachWithStep(step: Double, action: (Double) -> Unit) {
		var current = from
		while (current < to) {
			action(current)
			current += step
		}
	}
}