package com.github.hummel.dmma.lab4

import java.io.File
import java.security.SecureRandom
import javax.imageio.ImageIO

private val random = SecureRandom()

fun imageToPixelArray(fileName: String): IntArray {
	val classLoader = Perceptron::class.java.classLoader
	val file = File(classLoader.getResource(fileName)!!.file)
	val image = ImageIO.read(file)
	// Создаем массив для хранения пикселей (0 - белый, 1 - черный)
	val pixels = IntArray(image.width * image.height) { index ->
		val x = index % image.width
		val y = index / image.width
		if (image.getRGB(x, y) == 0xFFFFFFFF.toInt()) 0 else 1
	}
	return pixels
}

private fun <E> List<E>.randomItem(action: (item: E) -> Unit) {
	action(this[random.nextInt(size)])
}

data class PictureImage(val pixelArray: IntArray, var isTargetImage: Boolean) {
	constructor(fileName: String, isTargetImage: Boolean) : this(
		imageToPixelArray(fileName), isTargetImage
	)

	// Метод для вычисления суммы взвешенных значений пикселей
	fun calculateWeightSum(weights: IntArray): Int = weights.zip(pixelArray) { weight, pixel -> weight * pixel }.sum()

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

class Perceptron(imagePixelCount: Int, val iterationCount: Int) {
	private val weights: IntArray = IntArray(imagePixelCount)

	// Метод для обучения персептрона на заданном наборе обучающих изображений.
	fun train(trainingSet: List<PictureImage>) {
		repeat(iterationCount - 1) {
			trainingSet.randomItem {
				// Вычисляем вывод персептрона для выбранного изображения.
				val isTargetImage = isTargetImage(it)
				// Проверяем, совпадает ли вывод с правильным ответом
				if (it.isTargetImage != isTargetImage) {
					if (it.isTargetImage) {
						// Если вывод неверный и изображение является целевым, стимулируем персептрон.
						stimulate(it)
					} else {
						// Если вывод неверный и изображение не является целевым, наказываем персептрон.
						punish(it)
					}
				}
			}
		}
	}

	// Метод для наказания персептрона в случае неверного выхода.
	private fun punish(image: PictureImage) {
		// Проходим по всем пикселям изображения и уменьшаем соответствующие веса, если пиксель активен (1).
		weights.indices.asSequence().filter { image.pixelArray[it] == 1 }.forEach { weights[it]-- }
	}

	// Метод для стимулирования персептрона в случае неверного выхода.
	private fun stimulate(image: PictureImage) {
		// Проходим по всем пикселям изображения и увеличиваем соответствующие веса, если пиксель активен (1).
		weights.indices.asSequence().filter { image.pixelArray[it] == 1 }.forEach { weights[it]++ }
	}

	// Метод для вычисления выхода персептрона
	fun isTargetImage(image: PictureImage): Boolean = image.calculateWeightSum(weights) >= 0
}