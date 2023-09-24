package hummel

fun main() {
	// Создание списков изображений для каждой категории: S, N, T.
	val c1Images = ((0..3).map { PictureImage("train/0$it.jpg", 1) })
	val h1Images = ((0..3).map { PictureImage("train/1$it.jpg", 0) })
	val t1Images = ((0..3).map { PictureImage("train/2$it.jpg", 0) })
	val c2Images = ((0..3).map { PictureImage("train/0$it.jpg", 0) })
	val h2Images = ((0..3).map { PictureImage("train/1$it.jpg", 1) })
	val t2Images = ((0..3).map { PictureImage("train/2$it.jpg", 0) })
	val c3Images = ((0..3).map { PictureImage("train/0$it.jpg", 0) })
	val h3Images = ((0..3).map { PictureImage("train/1$it.jpg", 0) })
	val t3Images = ((0..3).map { PictureImage("train/2$it.jpg", 1) })

	// Объединение списков изображений для каждой категории.
	val cImages = c1Images + h1Images + t1Images
	val hImages = c2Images + h2Images + t2Images
	val tImages = c3Images + h3Images + t3Images

	// Создание трех персептронов для каждой категории.
	val perceptron1 = Perceptron(imagePixelCount = 36, iterationCount = 10000000)
	val perceptron2 = Perceptron(imagePixelCount = 36, iterationCount = 10000000)
	val perceptron3 = Perceptron(imagePixelCount = 36, iterationCount = 10000000)

	// Обучение первого персептрона на изображениях из категории S.
	perceptron1.train(cImages)

	// Обучение второго персептрона на изображениях из категории N.
	perceptron2.train(hImages)

	// Обучение третьего персептрона на изображениях из категории T.
	perceptron3.train(tImages)

	// Проверка наличия целевых изображений с помощью обученных персептронов.
	println("Распознано (C): ${perceptron1.isTargetImage(PictureImage("test/c-test.jpg", 1))}")
	println("Распознано (H): ${perceptron2.isTargetImage(PictureImage("test/h-test.jpg", 1))}")
	println("Распознано (T): ${perceptron3.isTargetImage(PictureImage("test/t-test.jpg", 1))}")
}