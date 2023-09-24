package hummel

import java.io.File
import java.util.*
import javax.imageio.ImageIO


private val random = Random()

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

// Расширение списка для выбора случайного элемента и выполнения действия над ним
private fun <E> List<E>.randomItem(action: (item: E) -> Unit) {
	action(this[random.nextInt(this.size)])
}

// Вывод матрицы весов
private fun IntArray.printInSixLines() {
	for (i in 0 until 6) {
		for (j in 0 until 6) {
			val formattedNumber = String.format("%3d", this[i * 6 + j])
			print("$formattedNumber ")
		}
		println()
	}
	println()
}

// Определяем класс для хранения изображения и метки
data class PictureImage(val pixelArray: IntArray, var isTargetImage: Boolean) {
	constructor(fileName: String, targetImageProbability: Boolean) : this(
		// Создаем объект PictureImage, используя функцию imageToPixelArray
		imageToPixelArray(fileName), targetImageProbability
	)

	// Метод для вычисления суммы взвешенных значений пикселей
	fun calculateWeightSum(weights: IntArray): Int {
		var sum = 0
		// Проходим по всем пикселям и умножаем их на соответствующие веса
		weights.indices.forEach {
			sum += weights[it] * pixelArray[it]
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
		result = 31 * result + isTargetImage.hashCode()
		return result
	}
}

// Определяем класс Perceptron для обучения
class Perceptron(imagePixelCount: Int, val iterationCount: Int) {
	private val weights: IntArray = IntArray(imagePixelCount)
	private var prev: IntArray = IntArray(imagePixelCount)

	// Метод для обучения персептрона на заданном наборе обучающих изображений.
	fun train(trainingSet: List<PictureImage>) {
		// Проходим через заданное количество итераций обучения.
		for (it in 1..iterationCount) {
			// Случайным образом выбираем изображение из обучающего набора.
			trainingSet.randomItem {
				// Вычисляем выход персептрона для выбранного изображения.
				val output = isTargetImage(it)
				// Проверяем, совпадает ли выход с желаемой меткой (целевым изображением).
				if (it.isTargetImage != output) {
					if (!it.isTargetImage) {
						// Если выход неверный и изображение не является целевым, наказываем персептрон.
						punish(it)
					} else {
						// Если выход неверный и изображение является целевым, стимулируем персептрон.
						stimulate(it)
					}
				}
			}
			val cur = weights.copyOf()
			if (!cur.contentEquals(prev)) {
				prev = cur
				cur.printInSixLines()
			}
		}
	}

	// Метод для наказания персептрона в случае неверного выхода.
	private fun punish(image: PictureImage) {
		// Проходим по всем пикселям изображения и уменьшаем соответствующие веса, если пиксель активен (1).
		weights.indices.forEach {
			if (image.pixelArray[it] == 1) {
				weights[it] -= 1
			}
		}
	}

	// Метод для стимулирования персептрона в случае неверного выхода.
	private fun stimulate(image: PictureImage) {
		// Проходим по всем пикселям изображения и увеличиваем соответствующие веса, если пиксель активен (1).
		weights.indices.forEach {
			if (image.pixelArray[it] == 1) {
				weights[it] += 1
			}
		}
	}

	// Метод для вычисления выхода персептрона
	fun isTargetImage(image: PictureImage): Boolean {
		return image.calculateWeightSum(weights) >= 0
	}
}