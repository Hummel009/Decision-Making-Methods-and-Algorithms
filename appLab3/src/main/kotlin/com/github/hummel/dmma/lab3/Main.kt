package com.github.hummel.dmma.lab3

// Константы, используемые для генерации данных и вычислений.
const val STEP: Double = 0.001
const val NUMBERS_COUNT: Int = 10000

const val PROBABILITY_1: Double = 0.8
const val MEAN_1: Double = 1.0
const val DERIVATION_1: Double = 3.0

const val PROBABILITY_2: Double = 1.0 - PROBABILITY_1
const val MEAN_2: Double = 4.0
const val DERIVATION_2: Double = 3.0

fun main() {
	// Генерация двух векторов с заданными параметрами.
	val firstVector = generateVector(NUMBERS_COUNT, MEAN_1, DERIVATION_1)
	val secondVector = generateVector(NUMBERS_COUNT, MEAN_2, DERIVATION_2)

	// Создание функций плотности вероятности на основе сгенерированных векторов и вероятностей.
	val firstFunction = generateProbabilityDensityFunction(
		firstVector.mean(), firstVector.standardDeviation(), PROBABILITY_1
	)
	val secondFunction = generateProbabilityDensityFunction(
		secondVector.mean(), secondVector.standardDeviation(), PROBABILITY_2
	)

	// Определение интервала значений X на основе объединения обоих векторов.
	val interval = getInterval(firstVector, secondVector)

	// Генерация значений X с заданным шагом.
	val xValues = doubleArrayFromRange(interval.first, interval.second, STEP)

	// Вычисление значений Y для обеих функций на основе X.
	val y1Values = xValues.map(firstFunction).toDoubleArray()
	val y2Values = xValues.map(secondFunction).toDoubleArray()

	// Нахождение места пересечения колоколов вероятности
	val firstIsBigger = y1Values[0] > y2Values[0]
	val separatorI = xValues.indices.filterIndexed { _, i ->
		if (firstIsBigger) y2Values[i] >= y1Values[i] else y1Values[i] > y2Values[i]
	}.first()
	println("arr[$separatorI]=${xValues[separatorI]}")

	// Вычисление областей под кривыми и отрисовка графика с информацией о долях ошибок.
	val areas = getAreas(y1Values, y2Values, STEP, xValues, separatorI)
	draw(xValues, y1Values, y2Values, areas)
}