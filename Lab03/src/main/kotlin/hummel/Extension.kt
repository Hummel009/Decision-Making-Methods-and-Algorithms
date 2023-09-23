package hummel

import kotlin.math.sqrt

// Расширение для DoubleArray, вычисляющее среднее значение элементов массива.
fun DoubleArray.mean(): Double {
	val sum = this.indices.sumOf { this[it] }
	return sum / this.size
}

// Расширение для DoubleArray, вычисляющее стандартное отклонение элементов массива.
fun DoubleArray.standardDeviation(): Double = sqrt(variance(this))

// Вспомогательная функция, вычисляющая дисперсию (variance) массива значений.
private fun variance(input: DoubleArray): Double {
	// Вычисляем среднее значение элементов массива.
	val mean = input.mean()

	// Вычисляем сумму квадратов разности каждого элемента и среднего значения.
	val variance = input.indices.map { input[it] - mean }.sumOf { it * it }

	// Делим сумму на количество элементов массива для получения дисперсии.
	return variance / input.size
}