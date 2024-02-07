package com.github.hummel.dmma.lab2

fun <T> Array<T>.deepEquals(other: Array<T>): Boolean = contentDeepEquals(other)

fun centroidOf(points: Array<Point>): Point {
	var centerX = 0.0
	var centerY = 0.0
	points.forEach { (x, y) ->
		centerX += x
		centerY += y
	}
	centerX /= points.size
	centerY /= points.size

	return Point(centerX.toInt(), centerY.toInt())
}

fun splitForClusters(points: Array<Point>, sites: Array<Point>): Array<Cluster> {
	// Создание массива кластеров
	val clusters = Array(sites.size) { mutableListOf<Point>() }
	// Проход по всем точкам и определение, к какому кластеру они принадлежат
	points.forEach { point ->
		var n = 0
		val closestSite = sites.minByOrNull { it.distanceTo(point) }
		val closestSiteDistance = closestSite?.distanceTo(point)

		closestSiteDistance?.let { dist ->
			val closestSiteIndex = sites.indexOfFirst { it.distanceTo(point) == dist }
			n = closestSiteIndex
		}
		clusters[n].add(point)
	}
	// Создание массива кластеров с информацией о кластерах
	return Array(sites.size) { index -> Cluster(sites[index], clusters[index].toTypedArray()) }
}

@Suppress("NAME_SHADOWING")
fun clusterByKMeans(points: Array<Point>, sites: Array<Point>): Array<Cluster> {
	var clusters: Array<Cluster>

	var sites = sites
	var i = 0
	do {
		i++
		val oldSites = sites
		clusters = splitForClusters(points, oldSites)
		sites = Array(oldSites.size) { index -> centroidOf(clusters[index].points) }
	} while (!oldSites.deepEquals(sites) && i < 100)
	return clusters
}