package hummel

import java.awt.Color
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage

val COLORS: Array<Color> = arrayOf(
	Color.RED,
	Color.GREEN,
	Color.BLUE,
	Color.CYAN,
	Color.YELLOW,
	Color.ORANGE,
	Color.MAGENTA,
	Color.PINK,
	Color(0xFF7F50),
	Color(0xC71585),
	Color(0xFA8072),
	Color(0xD8BFD8),
	Color(0x008080),
	Color(0x4682B4),
	Color(0xE0FFFF),
	Color(0xFFE4B5),
	Color(0xDB7093),
	Color(0xBC8F8F),
	Color(0xE6E6FA),
	Color(0x7FFFD4),
	Color(0x9D9FE7),
	Color(0xC24E59),
	Color(0x668A4B),
	Color(0x9932CC),
	Color(0x800000)
)

fun drawClustersOnImage(imageSize: Int, clusters: Array<Cluster>): BufferedImage {
	val image = BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB)
	val graphics = image.createGraphics()
	val colors = Array(clusters.size) { index -> COLORS[index % COLORS.size] }
	graphics.background = Color.WHITE
	graphics.clearRect(0, 0, imageSize, imageSize)
	clusters.forEachIndexed { i, (site, points) ->
		points.forEach {
			graphics.color = colors[i]
			val ellipse = Ellipse2D.Double((it.x - 2).toDouble(), (it.y - 2).toDouble(), 4.0, 4.0)
			graphics.fill(ellipse)
		}
		val ellipse = Ellipse2D.Double((site.x - 6).toDouble(), (site.y - 6).toDouble(), 12.0, 12.0)
		graphics.color = Color.BLACK
		graphics.fill(ellipse)
	}
	graphics.dispose()
	return image
}