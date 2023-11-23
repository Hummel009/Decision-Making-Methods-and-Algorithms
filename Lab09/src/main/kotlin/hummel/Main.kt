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
		} ?: run {
			println("The network does not exist!")
		}
	}

	private fun train(mode: Int) {
		network?.let {
			print("Enter the folder path (example: ./test/data): ")
			val path = readln()
			trainNetworkCommand(path, mode)
		} ?: run {
			println("The network does not exist!")
		}
	}

	private fun recognize() {
		network?.let { network ->
			print("Enter the image path (example: ./test/s-test.jpg): ")
			val name = readln()
			if (!name.contains(VALUE)) {
				name.utils()
				return
			}
			val file = File(name)
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
			println("The network does not exist!")
		}
	}

	private fun create() {
		print("Enter the height (example: 6): ")
		val height = nextIntSafe()
		print("Enter the width (example: 6): ")
		val width = nextIntSafe()
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
		} ?: run {
			println("The network does not exist!")
		}
	}
}