package com.github.hummel.dmma.lab1

import kotlin.math.hypot

// сравниваем содержимое массивов
fun <T> Array<T>.deepEquals(other: Array<T>): Boolean = contentDeepEquals(other)

// класс точки, функция расстояния погипотенузе
data class Point(val x: Int, val y: Int) {
	fun distanceTo(p: Point): Double = distanceTo(p.x, p.y)

	private fun distanceTo(x: Int, y: Int): Double = hypot((this.x - x).toDouble(), (this.y - y).toDouble())
}

data class Cluster(val site: Point, val points: Array<Point>) {
	override fun equals(other: Any?): Boolean {
		if (this === other) {
			return true
		}
		if (javaClass != other?.javaClass) {
			return false
		}

		other as Cluster

		if (site != other.site) {
			return false
		}
		if (!points.contentEquals(other.points)) {
			return false
		}

		return true
	}

	override fun hashCode(): Int {
		var result = site.hashCode()
		result = 31 * result + points.contentHashCode()
		return result
	}
}

// Функция для вычисления центроида (средней точки) класса
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

// Функция для разделения точек на кластеры
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

// Функция для кластеризации методом k-средних
@Suppress("NAME_SHADOWING")
fun clusterByKMeans(points: Array<Point>, sites: Array<Point>): Array<Cluster> {
	var clusters: Array<Cluster>

	// Инициализация переменной i для отслеживания количества итераций
	var sites = sites
	var i = 0
	do {
		i++
		val oldSites = sites
		// Разделение точек на кластеры
		clusters = splitForClusters(points, oldSites)
		// Пересчет положений центров кластеров
		sites = Array(oldSites.size) { centroidOf(clusters[it].points) }
	} while (!oldSites.deepEquals(sites) && i < 100) //пока не кончатся изменения или не минёт 100 операций
	return clusters
}