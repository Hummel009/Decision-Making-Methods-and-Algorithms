package hummel

import java.io.File
import java.util.*
import javax.imageio.ImageIO

const val IMAGE_SIZE: Int = 1000

fun main() {
	val pointCount = 20000 //количество образов, от 10 до 100к
	val clusterCount = 9 //количество классов, от 2 до 20
	val random = Random()
	val points = Array(pointCount) { Point(random.nextInt(IMAGE_SIZE), random.nextInt(IMAGE_SIZE)) }
	val sites = Array(clusterCount) { Point(random.nextInt(IMAGE_SIZE), random.nextInt(IMAGE_SIZE)) }
	val clusters = clusterByKMeans(points, sites)
	val image = drawClustersOnImage(IMAGE_SIZE, clusters)
	ImageIO.write(image, "png", File("images/k-means-${System.currentTimeMillis()}.png"))
}