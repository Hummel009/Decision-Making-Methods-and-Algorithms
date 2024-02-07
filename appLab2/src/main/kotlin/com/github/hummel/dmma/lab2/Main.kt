package com.github.hummel.dmma.lab2

import java.io.File
import java.security.SecureRandom
import java.util.*
import javax.imageio.ImageIO

const val IMAGE_SIZE: Int = 1000
val RANDOM: Random = SecureRandom()

fun main() {
	val resourcePath = "appLab2"
	val pointCount = 100000
	val points = Array(pointCount) { Point(RANDOM.nextInt(IMAGE_SIZE), RANDOM.nextInt(IMAGE_SIZE)) }
	print("Maximin calculating...")
	var clusters = clusterByMaximin(points)
	val sites = Array(clusters.size) { index -> clusters[index].site }
	print("Maximin drawing...")
	var image = drawClustersOnImage(IMAGE_SIZE, clusters)
	ImageIO.write(image, "png", File("$resourcePath/images/maximin-$pointCount.png"))
	print("K-means calculating...")
	clusters = clusterByKMeans(points, sites)
	print("K-means drawing...")
	image = drawClustersOnImage(IMAGE_SIZE, clusters)
	ImageIO.write(image, "png", File("$resourcePath/images/maximin-$pointCount-with-k-means.png"))
}