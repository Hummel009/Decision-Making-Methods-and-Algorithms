package hummel

import java.io.File
import java.util.*
import javax.imageio.ImageIO

fun imageToPixelArray(fileName: String): IntArray {
	val classLoader = Perceptron::class.java.classLoader
	val file = File(classLoader.getResource(fileName)!!.file)
	val image = ImageIO.read(file)
	// Создаем массив для хранения пикселей (0 - белый, 1 - черный)
	val pixels = IntArray(image.width * image.height)
	// Проходим по всем пикселям изображения
	for (x in 0 until image.width) {
		for (y in 0 until image.height) {
			// Преобразуем цвет пикселя в 0 или 1 и сохраняем в массиве
			pixels[x * image.width + y] = (if (image.getRGB(x, y) == 0xFFFFFFFF.toInt()) 0 else 1)
		}
	}
	// Возвращаем массив пикселей
	return pixels
}

// Определяем класс для хранения изображения и метки
data class PictureImage(val pixelArray: IntArray, var isTargetImage: Int) {
	constructor(fileName: String, targetImageProbability: Int) : this(
		// Создаем объект PictureImage, используя функцию imageToPixelArray
		imageToPixelArray(fileName), targetImageProbability
	)

	// Метод для вычисления суммы взвешенных значений пикселей
	fun calculateWeightSum(weights: IntArray): Int {
		var sum = 0
		// Проходим по всем пикселям и умножаем их на соответствующие веса
		weights.indices.forEach { i ->
			sum += weights[i] * pixelArray[i]
		}
		return sum
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) {
			return true
		}
		if (javaClass != other?.javaClass) {
			return false
		}

		other as PictureImage

		if (!pixelArray.contentEquals(other.pixelArray)) {
			return false
		}
		if (isTargetImage != other.isTargetImage) {
			return false
		}

		return true
	}

	override fun hashCode(): Int {
		var result = pixelArray.contentHashCode()
		result = 31 * result + isTargetImage
		return result
	}
}

// Определяем класс Perceptron для обучения
class Perceptron(
	imagePixelCount: Int, private val threshold: Double = 0.0, val iterationCount: Int = 10000
) {
	// Массив весов
	private val weights: IntArray = IntArray(imagePixelCount)

	// Метод для обучения персептрона на заданном наборе обучающих изображений.
	fun train(trainingSet: List<PictureImage>) {
		var prevPercent = -1
		// Проходим через заданное количество итераций обучения.
		for (it in 1..iterationCount) {
			// Случайным образом выбираем изображение из обучающего набора.
			trainingSet.randomItem { image ->
				// Вычисляем выход персептрона для выбранного изображения.
				val output = calculateOutput(image)
				// Проверяем, совпадает ли выход с желаемой меткой (целевым изображением).
				if (image.isTargetImage != output) {
					if (image.isTargetImage == 0) {
						// Если выход неверный и изображение не является целевым, наказываем персептрон.
						punish(image)
					} else {
						// Если выход неверный и изображение является целевым, стимулируем персептрон.
						stimulate(image)
					}
				}
			}
			// Вычисляем процент завершенности обучения и выводим информацию о процессе каждые 10%.
			val percent = (it.toDouble() / iterationCount * 100).toInt()
			if ((percent % 10 == 0) && prevPercent != percent) {
				prevPercent = percent
				println("Training... $percent%")
			}
		}
	}

	// Метод для наказания персептрона в случае неверного выхода.
	private fun punish(image: PictureImage) {
		// Проходим по всем пикселям изображения и уменьшаем соответствующие веса, если пиксель активен (1).
		weights.indices.forEach { i ->
			if (image.pixelArray[i] == 1) {
				weights[i] -= 1
			}
		}
	}

	// Метод для стимулирования персептрона в случае неверного выхода.
	private fun stimulate(image: PictureImage) {
		// Проходим по всем пикселям изображения и увеличиваем соответствующие веса, если пиксель активен (1).
		weights.indices.forEach { i ->
			if (image.pixelArray[i] == 1) {
				weights[i] += 1
			}
		}
	}

	// Метод для вычисления выхода персептрона
	private fun calculateOutput(image: PictureImage) = if (image.calculateWeightSum(weights) >= threshold) 1 else 0

	// Метод для проверки, является ли изображение целевым
	fun isTargetImage(image: PictureImage): Boolean = calculateOutput(image) == 1

	private val random = Random()

	// Расширение списка для выбора случайного элемента и выполнения действия над ним
	private fun <E> List<E>.randomItem(action: (item: E) -> Unit) {
		action(this[random.nextInt(this.size)])
	}
}