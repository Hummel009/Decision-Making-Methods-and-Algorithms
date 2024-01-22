package hummel

import org.encog.ml.data.basic.BasicMLData
import org.encog.neural.networks.BasicNetwork
import java.awt.image.BufferedImage
import java.io.File

fun main() {
	Launcher.init()
	while (true) {
		print("Enter the command: ")
		val command = readln()

		if ("exit" == command) {
			break
		}

		Launcher.functions[command]?.invoke() ?: println("Unknown command!")
	}
}

private const val networkNotExist = "The network does not exist!"

object Launcher {
	private var imageSize: Pair<Int, Int> = 6 to 6
	private var network: BasicNetwork? = null
	val functions: MutableMap<String, () -> Unit> = HashMap()

	fun init() {
		functions.clear()
		functions["commands"] = ::showAllCommands
		functions["create"] = ::create
		functions["recognize"] = ::recognize
		functions["train"] = { train(1) }
		functions["punish"] = { train(0) }
		functions["info"] = ::info
	}

	private fun info() {
		network?.let {
			println("The network is the perceptron for images ${imageSize.first}x${imageSize.second}.")
		} ?: run {
			println(networkNotExist)
		}
	}

	private fun train(mode: Int) {
		network?.let {
			print("Enter the folder path (example: ./test/data): ")
			val path = readln()
			trainNetworkCommand(path, mode)
		} ?: run {
			println(networkNotExist)
		}
	}

	private fun recognize() {
		network?.let { network ->
			print("Enter the image path (example: ./test/s-test.jpg): ")
			val name = readln()
			if (!name.contains(VALUE)) {
				utils()
				return@recognize
			}
			val file = File("appLab9", name)
			val image = file.loadImage()
			image?.let {
				if (it.height == imageSize.first && it.width == imageSize.second) {
					val flatten = it.flatten()
					val output = network.compute(BasicMLData(DoubleArray(flatten.size) { int ->
						flatten[int].toDouble()
					}))
					println("Result: ${output.data.contentToString()}.")
				} else {
					println("Invalid image size!")
				}
			} ?: run {
				println("The image does not exist!")
			}
		} ?: run {
			println(networkNotExist)
		}
	}

	private fun create() {
		print("Enter the height (example: 6): ")
		val height = readIntSafe()
		print("Enter the width (example: 6): ")
		val width = readIntSafe()
		imageSize = height to width
		network = createNetwork(height * width * 3, 1)
		println("The network was created.")
	}

	private fun showAllCommands() {
		functions.keys.forEach { println(it) }
	}

	private fun trainNetworkCommand(path: String, output: Int) {
		network?.let { network ->
			val dir = File("appLab9", path)
			if (dir.isDirectory) {
				val images = mutableListOf<BufferedImage>()
				dir.listFiles()?.forEach {
					val image = it.loadImage()
					image?.let { img ->
						if (img.height == imageSize.first && img.width == imageSize.second) {
							images.add(img)
						}
					}
				}
				val list = images.map { it.flatten() }
				network.train(list, output)
			} else {
				println("Invalid directory!")
			}
		} ?: run {
			println(networkNotExist)
		}
	}
}