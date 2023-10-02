package hummel

// Создание точки из двух координат
infix fun Double.to(y: Double): UnclassifiedPoint = UnclassifiedPoint(this, y)

// Присвоение класса точке
infix fun UnclassifiedPoint.classifiedBy(classIndex: Int): Point = Point(this.x, this.y, classIndex)

// Класс представляющий точку без классификации
data class UnclassifiedPoint(val x: Double, val y: Double)

// Класс представляющий точку с классификацией
data class Point(val x: Double, val y: Double, val classIndex: Int)

// Структура данных, содержащая функции для решений и построения графика
data class GeneratedFunctions(val decisionFunction: PotentialDecisionFunction, val chartFunction: ChartFunction)

// Коэффициенты для потенциальной функции
private val ERMIT_COEFFICIENTS = PotentialCoefficients(1.0, 4.0, 4.0, 16.0)
private val INITIAL_COEFFICIENTS = PotentialCoefficients(0.0, 0.0, 0.0, 0.0)

// Типы для удобства использования
typealias PotentialDecisionFunction = (UnclassifiedPoint) -> Point
typealias ChartFunction = (Double) -> Double

// Тип для представления потенциальной функции
private typealias PotentialFunction = (Point) -> Double

// Класс, представляющий коэффициенты потенциальной функции
data class PotentialCoefficients(val first: Double, val second: Double, val third: Double, val fourth: Double) {

	// Перегрузка операторов для удобства работы с коэффициентами
	operator fun plus(other: PotentialCoefficients): PotentialCoefficients =
		PotentialCoefficients(first + other.first, second + other.second, third + other.third, fourth + other.fourth)

	operator fun times(c: Double): PotentialCoefficients =
		PotentialCoefficients(c * first, c * second, c * third, c * fourth)

	// Подстановка значений точки в коэффициенты
	fun substitutePoint(point: Point): PotentialCoefficients =
		PotentialCoefficients(first, second * point.x, third * point.y, fourth * point.x * point.y)

	// Получение потенциальной функции
	fun getPotentialFunction(): PotentialFunction = { (x, y, _) ->
		first + second * x + third * y + fourth * x * y
	}

	// Получение функции для построения графика
	fun getChartFunction(): (Double) -> Double = {
		-(first + second * it) / (third + fourth * it)
	}

	// Строковое представление коэффициентов
	override fun toString(): String {
		return "$first + ($second)*X + ($third)*Y + ($fourth)*X*Y"
	}
}

// Функция для получения коэффициента нормализации
private fun getNormalizationCoefficient(potentialValue: Double, classIndex: Int) =
	if (classIndex == 1 && potentialValue <= 0) {
		1.0
	} else if (classIndex == 2 && potentialValue > 0) {
		-1.0
	} else {
		0.0
	}

// Генерация функции принятия решений и функции для графика
fun generateDecisionFunction(
	trainSet: Array<Point>, initialPotentialCoefficients: PotentialCoefficients = INITIAL_COEFFICIENTS
): GeneratedFunctions = generateDecisionFunction(trainSet.asList(), initialPotentialCoefficients)

fun generateDecisionFunction(
	trainSet: List<Point>, initialPotentialCoefficients: PotentialCoefficients = INITIAL_COEFFICIENTS
): GeneratedFunctions {
	var potentialCoefficients = initialPotentialCoefficients
	var normalizationCoefficient = 1.0
	for (i in 0..(trainSet.size - 2)) {
		val localPotentialCoefficients = ERMIT_COEFFICIENTS.substitutePoint(trainSet[i])
		potentialCoefficients += (localPotentialCoefficients * normalizationCoefficient)
		val potentialValue = potentialCoefficients.getPotentialFunction()(trainSet[i + 1])
		normalizationCoefficient = getNormalizationCoefficient(potentialValue, trainSet[i + 1].classIndex)
	}
	println("Potential function: $potentialCoefficients")
	return GeneratedFunctions({ (x, y) ->
		val potentialValue =
			potentialCoefficients.first + potentialCoefficients.second * x + potentialCoefficients.third * y + potentialCoefficients.fourth * x * y
		if (potentialValue <= 0) {
			Point(x, y, 2)
		} else {
			Point(x, y, 1)
		}
	}, potentialCoefficients.getChartFunction())
}