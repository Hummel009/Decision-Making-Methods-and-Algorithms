package hummel

import org.encog.Encog
import org.encog.ml.data.basic.BasicMLData
import org.encog.neural.networks.BasicNetwork
import org.meinkopf.console.BasicCommandList
import org.meinkopf.console.JConsole
import java.awt.image.BufferedImage
import java.io.File
import kotlin.system.exitProcess

fun main() {
	JConsole.PROMPT = "> "
	JConsole.HEADER = "Neural network app"
	FixedJConsole(populateCommandList()).run()
}

var imageSize: Pair<Int, Int> = 100 to 100
var network: BasicNetwork? = null

fun populateCommandList(): BasicCommandList {
	val commandList = BasicCommandList()
	commandList.register("exit") {
		Encog.getInstance().shutdown()
		exitProcess(0)
	}
	commandList.register("help") {
		println(" | exit        -> exit program")
		println(" | help        -> print this help")
		println(" | recognize")
		println("    | path         -> recognize image from this path")
		println(" | traindir")
		println("    | path         -> train network with all images from specified dir")
		println(" | punishdir")
		println("    | path         -> punish network with all images from specified dir")
		println(" | network")
		println("    | ?            -> show network info")
		println("    | height width -> create network with for specified image size")
	}
	commandList.register("network") {
		if (it.isNotEmpty()) {
			if (it[0].toString() == "?") {
				if (network != null) {
					println("Multi Layer Perceptron [Height: ${imageSize.first}, Width: ${imageSize.second}]")
				} else {
					println("No network")
				}
			} else {
				if (it.size >= 2) {
					val height = it[0].safeToInt(true)
					val width = it[1].safeToInt(true)
					if (height != null && width != null) {
						imageSize = height to width
						println("Creating network...")
						network = createNetwork(height * width * 3, 1)
						println("Done")
					}
				} else {
					println("Image height and width required")
				}
			}
		} else {
			println(" | ?            -> show size")
			println(" | height width -> set new size")
		}
	}
	commandList.register("recognize") {
		if (it.isNotEmpty()) {
			val image = loadImageFromFile(it[0].toString())
			if (image != null) {
				if (image.height == imageSize.first && image.width == imageSize.second) {
					if (network != null) {
						val flatten = image.flatten()
						println("Recognizing...")
						val output = network!!.compute(BasicMLData(DoubleArray(flatten.size) { i ->
							flatten[i].toDouble()
						}))
						println("Result: ${output.data.contentToString()}")
					} else {
						println("Network not created")
					}
				} else {
					println("Invalid image size (${image.height},${image.width}), required (${imageSize.first},${imageSize.second})")
				}
			} else {
				println("Cannot load image from this path")
			}
		} else {
			println("Image path required")
		}
	}
	commandList.register("traindir") {
		trainNetworkCommand(it, 1)
	}
	commandList.register("punishdir") {
		trainNetworkCommand(it, 0)
	}
	return commandList
}

fun trainNetworkCommand(it: Array<Any>, output: Int) {
	if (network != null) {
		if (it.isNotEmpty()) {
			try {
				val dir = File(it[0].toString())
				if (dir.isDirectory) {
					println("Reading directory...")
					val images = mutableListOf<BufferedImage>()
					dir.listFiles()?.forEach {
						val image = loadImageFromFile(it)
						if (image != null && image.height == imageSize.first && image.width == imageSize.second) {
							images.add(image)
						}
					}
					println("Read ${images.size} images")
					println("Flattening...")
					val list = images.map { it.flatten() }
					println("Training...")
					(network ?: return).train(list, output)
				} else {
					println("Not a directory")
				}
			} catch (e: Exception) {
				println("Error while reading directory")
				e.printStackTrace()
			}
		} else {
			println("Directory path required")
		}
	} else {
		println("Network not created")
	}
}
