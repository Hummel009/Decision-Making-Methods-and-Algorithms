package hummel

import kotlin.math.sqrt

fun DoubleArray.mean(): Double {
	val sum = this.indices.sumOf { this[it] }
	return sum / this.size
}

fun DoubleArray.standardDeviation(): Double {
	return sqrt(variance(this))
}

private fun variance(input: DoubleArray): Double {
	val mean = input.mean()
	val variance = input.indices.map { input[it] - mean }.sumOf { it * it }
	return variance / input.size
}
