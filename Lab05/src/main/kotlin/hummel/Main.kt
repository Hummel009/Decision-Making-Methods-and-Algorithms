package hummel

import java.util.*

// Инициализация объекта для генерации случайных чисел
val RANDOM: Random = Random()

// Класс, представляющий диапазон значений
data class Range(val from: Double, val to: Double) {

	// Генерация случайного числа в пределах диапазона
	fun randomInto(): Double = from + (to - from) * RANDOM.nextDouble()

	// Выполнение действия для каждого значения с шагом
	fun forEachWithStep(step: Double, action: (currentValue: Double) -> Unit) {
		var current = from
		while (current < to) {
			action(current)
			current += step
		}
	}
}

// Задание диапазонов для координат X и Y
val X_RANGE: Range = Range(-5.0, 5.0)
val Y_RANGE: Range = Range(-3.0, 3.0)

// Задание обучающего набора по умолчанию
val TRAIN_SET_DEFAULT: Array<Point> = arrayOf(
	-1.0 to 0.0 classifiedBy 1, 1.0 to 1.0 classifiedBy 1, 2.0 to 0.0 classifiedBy 2, 1.0 to -2.0 classifiedBy 2
)

// Задание обучающего набора для пересекающихся линий
val TRAIN_DATA_INTERSECTED_LINES: Array<Point> = arrayOf(
	-1.0 to -1.0 classifiedBy 1, 1.0 to 1.0 classifiedBy 1, -1.0 to 1.0 classifiedBy 2, 1.0 to -1.0 classifiedBy 2
)

// Количество случайных точек для генерации
const val POINT_NUMBER: Int = 1000

// Основная функция
fun main() {
	// Массив обучающих наборов
	val dataSets = arrayOf(TRAIN_SET_DEFAULT, TRAIN_DATA_INTERSECTED_LINES)

	// Итерация по обучающим наборам
	dataSets.forEachIndexed { index, dataSet ->
		// Генерация функции принятия решений и функции для графика
		val (decisionFunction, chartFunction) = generateDecisionFunction(dataSet)

		// Генерация случайных неклассифицированных точек
		val points = mutableListOf<UnclassifiedPoint>()
		for (i in 1..POINT_NUMBER) {
			points.add(
				X_RANGE.randomInto() to Y_RANGE.randomInto()
			)
		}

		// Классификация сгенерированных точек и отрисовка графика
		val classifiedPoints = points.map { decisionFunction(it) }
		draw(classifiedPoints, X_RANGE, Y_RANGE, chartFunction, index)
	}
}