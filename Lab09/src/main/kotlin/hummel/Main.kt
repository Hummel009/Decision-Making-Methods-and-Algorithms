package hummel

import org.encog.ml.data.basic.BasicMLData
import org.encog.neural.networks.BasicNetwork
import java.awt.image.BufferedImage
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.*

val scanner: Scanner = Scanner(System.`in`, StandardCharsets.UTF_8.name())

fun main() {
	Launcher.init()
	loop@ while (true) {
		print("Enter the command: ")
		val command = scanner.nextLine()

		if (command == "exit") {
			break@loop
		}

		Launcher.functions[command]?.invoke() ?: println("Unknown command!")
	}
	scanner.close()
}

object Launcher {
	private var imageSize: Pair<Int, Int> = 6 to 6
	private var network: BasicNetwork? = null
	val functions: MutableMap<String, () -> Unit> = HashMap()

	fun init() {
		functions.clear()
		functions["commands"] = this::showAllCommands
		functions["create"] = this::create
		functions["recognize"] = this::recognize
		functions["train"] = { train(1) }
		functions["punish"] = { train(0) }
		functions["info"] = this::info
	}

	private fun info() {
		network?.let {
			println("The network is the perceptron for images ${imageSize.first}x${imageSize.second}.")
		} ?: {
			println("The network does not exist!")
		}
	}

	private fun train(mode: Int) {
		network?.let {
			print("Enter the folder path (example: ./test/data): ")
			val path = scanner.nextLine()
			trainNetworkCommand(path, mode)
		} ?: {
			println("The network does not exist!")
		}
	}

	private fun recognize() {
		network?.let { network ->
			print("Enter the image path (example: ./test/s-test.jpg): ")
			val name = scanner.nextLine()
			val file = File(name)
			val image = file.loadImage()
			image?.let {
				if (image.height == imageSize.first && image.width == imageSize.second) {
					val flatten = image.flatten()
					val output = network.compute(BasicMLData(DoubleArray(flatten.size) {
						flatten[it].toDouble()
					}))
					println("Result: ${output.data.contentToString()}.")
				} else {
					println("Invalid image size!")
				}
			} ?: {
				println("The image does not exist!")
			}
		} ?: {
			println("The network does not exist!")
		}
	}

	private fun create() {
		print("Enter the height (example: 6): ")
		val height = scanner.nextIntSafe()
		print("Enter the width (example: 6): ")
		val width = scanner.nextIntSafe()
		imageSize = height to width
		network = createNetwork(height * width * 3, 1)
		println("The network was created.")
	}

	private fun showAllCommands() {
		for (item in functions.keys) {
			println(item)
		}
	}

	private fun trainNetworkCommand(path: String, output: Int) {
		network?.let { network ->
			val dir = File(path)
			if (dir.isDirectory) {
				val images = mutableListOf<BufferedImage>()
				dir.listFiles()?.forEach {
					val image = it.loadImage()
					image?.let {
						if (image.height == imageSize.first && image.width == imageSize.second) {
							images.add(image)
						}
					}
				}
				val list = images.map { it.flatten() }
				network.train(list, output)
			} else {
				println("Invalid directory!")
			}
		} ?: {
			println("The network does not exist!")
		}
	}
}