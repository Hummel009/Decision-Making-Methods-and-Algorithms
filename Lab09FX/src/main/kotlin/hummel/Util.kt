package hummel

import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

fun Any.safeToInt(onlyPositive: Boolean = false): Int? = try {
	val int = this.toString().toInt()
	if (onlyPositive) if (int > 0) int else null
	else int
} catch (e: NumberFormatException) {
	e.printStackTrace()
	null
}

fun loadImageFromFile(path: String): BufferedImage? = try {
	loadImageFromFile(File(path))
} catch (e: IOException) {
	e.printStackTrace()
	null
}

fun loadImageFromFile(file: File): BufferedImage? = try {
	ImageIO.read(file)
} catch (e: IOException) {
	e.printStackTrace()
	null
}

fun BufferedImage.flatten(): IntArray {
	val reds = mutableListOf<Int>()
	val greens = mutableListOf<Int>()
	val blues = mutableListOf<Int>()
	(0 until this.height).forEach { y ->
		(0 until this.width).forEach { x ->
			val rgb = this.getRGB(x, y)
			reds.add((rgb shr 16) and 0x000000FF)
			greens.add((rgb shr 8) and 0x000000FF)
			blues.add((rgb) and 0x000000FF)
		}
	}
	return (reds + greens + blues).toIntArray()
}
