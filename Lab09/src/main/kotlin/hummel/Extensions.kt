package hummel

import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.util.*
import javax.imageio.ImageIO

fun Scanner.nextIntSafe(): Int {
	return try {
		nextLine().toInt()
	} catch (e: Exception) {
		print("Error! Enter the correct value: ")
		nextIntSafe()
	}
}

fun File.loadImage(): BufferedImage? {
	return try {
		ImageIO.read(this)
	} catch (e: IOException) {
		e.printStackTrace()
		null
	}
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