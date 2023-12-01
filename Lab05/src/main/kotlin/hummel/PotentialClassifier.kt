package hummel

typealias DecisionFunction = (UnclassifiedPoint) -> ClassifiedPoint
typealias ChartFunction = (Double) -> Double
typealias PotentialFunction = (ClassifiedPoint) -> Double

data class UnclassifiedPoint(val x: Double, val y: Double)
data class ClassifiedPoint(val x: Double, val y: Double, val classIndex: Int)
data class GeneratedFunctions(val decisionFunction: DecisionFunction, val chartFunction: ChartFunction)

private val ermitCoefficients = PotentialCoefficients(1.0, 4.0, 4.0, 16.0)
private val initialCoefficients = PotentialCoefficients(0.0, 0.0, 0.0, 0.0)

fun generateDecisionFunction(
	trainSet: List<ClassifiedPoint>, initialPotentialCoefficients: PotentialCoefficients = initialCoefficients
): GeneratedFunctions {
	var potentialCoefficients = initialPotentialCoefficients
	var normalizationCoefficient = 1.0
	for (i in 0 until (trainSet.size - 1)) {
		val localPotentialCoefficients = ermitCoefficients.substitutePoint(trainSet[i])
		potentialCoefficients += (localPotentialCoefficients * normalizationCoefficient)
		val potentialValue = potentialCoefficients.getPotentialFunction()(trainSet[i + 1])
		normalizationCoefficient = getNormalizationCoefficient(potentialValue, trainSet[i + 1].classIndex)
	}
	println("Potential function: $potentialCoefficients")
	return GeneratedFunctions({ (x, y) ->
		val potentialValue =
			potentialCoefficients.first + potentialCoefficients.second * x + potentialCoefficients.third * y + potentialCoefficients.fourth * x * y

		ClassifiedPoint(x, y, if (potentialValue <= 0) 2 else 1)
	}, potentialCoefficients.getChartFunction())
}

private fun getNormalizationCoefficient(potentialValue: Double, classIndex: Int): Double {
	return if (classIndex == 1 && potentialValue <= 0) {
		1.0
	} else if (classIndex == 2 && potentialValue > 0) {
		-1.0
	} else {
		0.0
	}
}

data class PotentialCoefficients(val first: Double, val second: Double, val third: Double, val fourth: Double) {
	operator fun plus(other: PotentialCoefficients): PotentialCoefficients {
		return PotentialCoefficients(
			first + other.first, second + other.second, third + other.third, fourth + other.fourth
		)
	}

	operator fun times(c: Double): PotentialCoefficients =
		PotentialCoefficients(c * first, c * second, c * third, c * fourth)

	fun substitutePoint(point: ClassifiedPoint): PotentialCoefficients =
		PotentialCoefficients(first, second * point.x, third * point.y, fourth * point.x * point.y)

	fun getPotentialFunction(): PotentialFunction = { (x, y, _) -> first + second * x + third * y + fourth * x * y }

	fun getChartFunction(): ChartFunction = { -(first + second * it) / (third + fourth * it) }

	override fun toString(): String = "$first + ($second)*X + ($third)*Y + ($fourth)*X*Y"
}