package hummel

import java.io.File
import java.util.*
import javax.imageio.ImageIO

const val IMAGE_SIZE: Int = 1000

private val random = Random()

fun randomInt(bound: Int = Int.MAX_VALUE): Int = random.nextInt(bound)

fun main() {
	val pointCount = 90000
	val clusterCount = 9
	val points = Array(pointCount) { _ ->
		Point(randomInt(IMAGE_SIZE), randomInt(IMAGE_SIZE))
	}
	val sites = Array(clusterCount) { _ ->
		Point(randomInt(IMAGE_SIZE), randomInt(IMAGE_SIZE))
	}
	val clusters = clusterByKMeans(points, sites)
	val image = drawClustersOnImage(IMAGE_SIZE, clusters)
	ImageIO.write(image, "png", File("images/k-means-${System.currentTimeMillis()}.png"))
}