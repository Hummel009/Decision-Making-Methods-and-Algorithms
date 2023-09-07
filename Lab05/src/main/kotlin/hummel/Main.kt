package hummel

import java.util.*

val RANDOM: Random = Random()

data class Range(val from: Double, val to: Double) {
	fun randomInto(): Double = from + (to - from) * RANDOM.nextDouble()

	fun forEachWithStep(step: Double, action: (currentValue: Double) -> Unit) {
		var current = from
		while (current < to) {
			action(current)
			current += step
		}
	}
}

val X_RANGE: Range = Range(-5.0, 5.0)
val Y_RANGE: Range = Range(-3.0, 3.0)
val TRAIN_SET_DEFAULT: Array<Point> = arrayOf(
	-1.0 to 0.0 classifiedBy 1, 1.0 to 1.0 classifiedBy 1, 2.0 to 0.0 classifiedBy 2, 1.0 to -2.0 classifiedBy 2
)

val TRAIN_DATA_INTERSECTED_LINES: Array<Point> = arrayOf(
	-1.0 to -1.0 classifiedBy 1, 1.0 to 1.0 classifiedBy 1, -1.0 to 1.0 classifiedBy 2, 1.0 to -1.0 classifiedBy 2
)

const val POINT_NUMBER: Int = 1000

fun main() {
	val dataSets = arrayOf(TRAIN_SET_DEFAULT, TRAIN_DATA_INTERSECTED_LINES)
	dataSets.forEachIndexed { index, dataSet ->
		val (decisionFunction, chartFunction) = generateDecisionFunction(dataSet)
		val points = mutableListOf<UnclassifiedPoint>()
		for (i in 1..POINT_NUMBER) {
			points.add(
				X_RANGE.randomInto() to Y_RANGE.randomInto()
			)
		}
		val classifiedPoints = points.map { decisionFunction(it) }
		draw(classifiedPoints, X_RANGE, Y_RANGE, chartFunction, index)
	}
}
