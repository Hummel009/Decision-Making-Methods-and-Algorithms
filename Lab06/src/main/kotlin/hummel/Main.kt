package hummel

import com.apporiented.algorithm.clustering.Cluster
import com.apporiented.algorithm.clustering.DefaultClusteringAlgorithm
import com.apporiented.algorithm.clustering.visualization.DendrogramPanel
import java.awt.BorderLayout
import java.awt.GridLayout
import java.awt.Label
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants

val SAMPLE_DATA: Array<DoubleArray> = arrayOf(
	doubleArrayOf(.0, 5.0, .5, 2.0),
	doubleArrayOf(5.0, .0, 1.0, .6),
	doubleArrayOf(.5, 1.0, .0, 2.5),
	doubleArrayOf(2.0, .6, 2.5, .0)
)

val SAMPLE_DATA_INVERTED: Array<DoubleArray> = arrayOf(
	doubleArrayOf(.0, 1 / 5.0, 1 / .5, 1 / 2.0),
	doubleArrayOf(1 / 5.0, .0, 1 / 1.0, 1 / .6),
	doubleArrayOf(1 / .5, 1 / 1.0, .0, 1 / 2.5),
	doubleArrayOf(1 / 2.0, 1 / .6, 1 / 2.5, .0)
)

val NAMES: Array<String> = arrayOf("x1", "x2", "x3", "x4")

fun main() {
	val alg = DefaultClusteringAlgorithm()
	val clusterMin = alg.performClustering(SAMPLE_DATA, NAMES) { it.min() }
	val clusterMax = alg.performClustering(SAMPLE_DATA_INVERTED, NAMES) { it.min() }
	(clusterMin to clusterMax).show()
}

fun <T> Pair<Cluster<T>, Cluster<T>>.show(width: Int = 1000, height: Int = 600) {
	val f = JFrame()
	f.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
	f.layout = BorderLayout()
	val container = JPanel()
	container.layout = GridLayout(1, 2)
	container.add(this.first.getPanel("Standard"))
	container.add(this.second.getPanel("Inverted"))
	f.contentPane.add(container)
	f.title = "Hierarchical Clustering"
	f.setSize(width, height)
	f.setLocationRelativeTo(null)
	f.isVisible = true
}

fun <T> Cluster<T>.getPanel(
	label: String = "Cluster", scaleInterval: Double = 0.1, precision: Int = 2
): DendrogramPanel<T> = with(DendrogramPanel<T>()) {
	add(Label(label))
	isShowScale = true
	scaleValueInterval = scaleInterval
	scaleValueDecimals = precision
	borderLeft = 50
	borderRight = 30
	borderBottom = 25
	borderTop = 25
	setShowDistances(true)
	model = this@getPanel
	this
}