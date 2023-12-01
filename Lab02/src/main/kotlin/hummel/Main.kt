package hummel

import java.io.File
import java.util.*
import javax.imageio.ImageIO

const val IMAGE_SIZE: Int = 1000
val RANDOM: Random = Random()

fun main() {
	val pointCount = 100000
	val points = Array(pointCount) { Point(RANDOM.nextInt(IMAGE_SIZE), RANDOM.nextInt(IMAGE_SIZE)) }
	print("Maximin calculating...")
	var clusters = clusterByMaximin(points)
	val sites = Array(clusters.size) { index -> clusters[index].site }
	print("Maximin drawing...")
	var image = drawClustersOnImage(IMAGE_SIZE, clusters)
	ImageIO.write(image, "png", File("images/maximin-$pointCount.png"))
	print("K-means calculating...")
	clusters = clusterByKMeans(points, sites)
	print("K-means drawing...")
	image = drawClustersOnImage(IMAGE_SIZE, clusters)
	ImageIO.write(image, "png", File("images/maximin-$pointCount-with-k-means.png"))
}