package hummel

import java.util.*
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sqrt

private val random = Random()

// Генерирует массив значений типа Double в заданном диапазоне с определенным шагом.
fun doubleArrayFromRange(start: Double, end: Double, step: Double): DoubleArray {
	return generateSequence(start) { it + step }.takeWhile { it <= end }.toList().toDoubleArray()
}

// Генерирует случайное число из нормального распределения с заданным средним и стандартным отклонением.
fun gaussianRandomNumber(mean: Double, derivation: Double): Double = random.nextGaussian() * derivation + mean

// Генерирует массив значений типа Double заданной длины из нормального распределения.
fun generateVector(length: Int, mean: Double, derivation: Double): DoubleArray {
	return DoubleArray(length) { gaussianRandomNumber(mean, derivation) }
}

// Возвращает минимальное и максимальное значения из объединения двух массивов.
fun getInterval(firstVector: DoubleArray, secondVector: DoubleArray): Pair<Double, Double> {
	val allPoints = firstVector.plus(secondVector)
	return allPoints.min() to allPoints.max()
}

val sqrt2PI: Double = sqrt(2 * Math.PI)

// Функция для вычисления значения нормального распределения в точке x с заданным средним и стандартным отклонением.
fun gaussian(x: Double, mean: Double, derivation: Double): Double {
	var result = 1 / (derivation * sqrt2PI)
	result *= exp(-0.5 * ((x - mean) / derivation).pow(2.0))
	return result
}

// Генерирует функцию плотности вероятности нормального распределения с заданными параметрами и вероятностью.
fun generateProbabilityDensityFunction(
	vectorMean: Double, vectorDerivation: Double, probability: Double
): (Double) -> Double {
	return { x: Double -> gaussian(x, vectorMean, vectorDerivation) * probability }
}

// Вычисляет площадь под кривыми, представленными двумя массивами значений y1 и y2 с заданным шагом.
fun getAreas(y1Values: DoubleArray, y2Values: DoubleArray, step: Double): Pair<Double, Double> {
	var detectionMistake = 0.0
	var falsePositive = 0.0
	for (i in y1Values.indices) {
		if (y1Values[i] > y2Values[i]) {
			detectionMistake += y1Values[i] * step
		} else {
			falsePositive += y2Values[i] * step
		}
	}
	return detectionMistake to falsePositive
}