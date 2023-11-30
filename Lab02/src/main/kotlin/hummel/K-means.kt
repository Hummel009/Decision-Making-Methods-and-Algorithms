package hummel

fun <T> Array<T>.deepEquals(other: Array<T>): Boolean = this.contentDeepEquals(other)

fun centroidOf(points: Array<Point>): Point {
	var centerX = 0.0
	var centerY = 0.0
	for ((x, y) in points) {
		centerX += x
		centerY += y
	}
	centerX /= points.size
	centerY /= points.size

	return Point(centerX.toInt(), centerY.toInt())
}

fun splitForClusters(points: Array<Point>, sites: Array<Point>): Array<Cluster> {
	val clusters = Array(sites.size) { mutableListOf<Point>() }
	for (point in points) {
		var n = 0
		(0..sites.lastIndex).asSequence().filter {
			sites[it].distanceTo(point) < sites[n].distanceTo(point)
		}.forEach { n = it }
		clusters[n].add(point)
	}
	return Array(sites.size) { index ->
		Cluster(sites[index], clusters[index].toTypedArray())
	}
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