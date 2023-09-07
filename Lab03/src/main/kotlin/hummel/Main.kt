package hummel

const val STEP: Double = 0.001
const val NUMBERS_COUNT: Int = 10000

const val PROBABILITY_1: Double = 0.8
const val MEAN_1: Double = 1.0
const val DERIVATION_1: Double = 3.0

const val PROBABILITY_2: Double = 1.0 - PROBABILITY_1
const val MEAN_2: Double = 4.0
const val DERIVATION_2: Double = 3.0

fun main() {
	val firstVector = generateVector(NUMBERS_COUNT, MEAN_1, DERIVATION_1)
	val secondVector = generateVector(NUMBERS_COUNT, MEAN_2, DERIVATION_2)
	val firstFunction =
		generateProbabilityDensityFunction(firstVector.mean(), firstVector.standardDeviation(), PROBABILITY_1)
	val secondFunction =
		generateProbabilityDensityFunction(secondVector.mean(), secondVector.standardDeviation(), PROBABILITY_2)
	val interval = getInterval(firstVector, secondVector)
	val xValues = doubleArrayFromRange(interval.first, interval.second, STEP)
	val y1Values = xValues.map(firstFunction).toDoubleArray()
	val y2Values = xValues.map(secondFunction).toDoubleArray()
	val areas = getAreas(y1Values, y2Values, STEP)
	draw(xValues, y1Values, y2Values, areas)
}

