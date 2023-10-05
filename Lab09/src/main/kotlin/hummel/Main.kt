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
	private var imageSize: Pair<Int, Int> = 100 to 100
	private var network: BasicNetwork? = null
	val functions: MutableMap<String, () -> Unit> = HashMap()

	fun init() {
		functions.clear()
		functions["commands"] = this::showAllCommands
		functions["create"] = this::create
		functions["recognize"] = this::recognize
		functions["train"] = this::train
		functions["punish"] = this::punish
		functions["info"] = this::info
	}

	private fun info() {
		if (network != null) {
			println("The network is the perceptron for images ${imageSize.first}x${imageSize.second}.")
		} else {
			println("The network does not exist!")
		}
	}

	private fun train() {
		if (network != null) {
			print("Enter the folder path (example: ./test/data): ")
			val path = scanner.nextLine()
			trainNetworkCommand(path, 1)
		} else {
			println("The network does not exist!")
		}
	}

	private fun punish() {
		if (network != null) {
			print("Enter the folder path (example: ./test/data): ")
			val path = scanner.nextLine()
			trainNetworkCommand(path, 0)
		} else {
			println("The network does not exist!")
		}
	}

	private fun recognize() {
		if (network != null) {
			print("Enter the image path (example: ./test/s-test.jpg): ")
			val name = scanner.nextLine()
			val file = File(name)
			val image = file.loadImage()
			if (image != null) {
				if (image.height == imageSize.first && image.width == imageSize.second) {
					if (network != null) {
						val flatten = image.flatten()
						println("Recognizing...")
						val output = (network ?: return).compute(BasicMLData(DoubleArray(flatten.size) { i ->
							flatten[i].toDouble()
						}))
						println("Result: ${output.data.contentToString()}.")
					} else {
						println("The network does not exist!")
					}
				} else {
					println("Invalid image size!")
				}
			} else {
				println("The image does not exist!")
			}
		} else {
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

	private fun trainNetworkCommand(it: String, output: Int) {
		if (network != null) {
			val dir = File(it)
			if (dir.isDirectory) {
				val images = mutableListOf<BufferedImage>()
				dir.listFiles()?.forEach {
					val image = it.loadImage()
					if (image != null && image.height == imageSize.first && image.width == imageSize.second) {
						images.add(image)
					}
				}
				val list = images.map { it.flatten() }
				(network ?: return).train(list, output)
			} else {
				println("Invalid directory!")
			}
		} else {
			println("The network does not exist!")
		}
	}
}