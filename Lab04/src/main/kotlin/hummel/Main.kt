package hummel

fun main() {
	// Создание списков изображений для каждой категории: S, N, T.
	val c1Images = ((0..3).map { PictureImage("train/0$it.jpg", true) })
	val h1Images = ((0..3).map { PictureImage("train/1$it.jpg", false) })
	val t1Images = ((0..3).map { PictureImage("train/2$it.jpg", false) })
	val c2Images = ((0..3).map { PictureImage("train/0$it.jpg", false) })
	val h2Images = ((0..3).map { PictureImage("train/1$it.jpg", true) })
	val t2Images = ((0..3).map { PictureImage("train/2$it.jpg", false) })
	val c3Images = ((0..3).map { PictureImage("train/0$it.jpg", false) })
	val h3Images = ((0..3).map { PictureImage("train/1$it.jpg", false) })
	val t3Images = ((0..3).map { PictureImage("train/2$it.jpg", true) })

	// Объединение списков изображений для каждой категории.
	val cImages = c1Images + h1Images + t1Images
	val hImages = c2Images + h2Images + t2Images
	val tImages = c3Images + h3Images + t3Images

	// Создание трех персептронов для каждой категории.
	val perceptron1 = Perceptron(imagePixelCount = 36, iterationCount = 50)
	val perceptron2 = Perceptron(imagePixelCount = 36, iterationCount = 50)
	val perceptron3 = Perceptron(imagePixelCount = 36, iterationCount = 50)

	// Обучение первого персептрона на изображениях из категории S.
	println("training 1")
	perceptron1.train(cImages)

	// Обучение второго персептрона на изображениях из категории N.
	println("training 2")
	perceptron2.train(hImages)

	// Обучение третьего персептрона на изображениях из категории T.
	println("training 3")
	perceptron3.train(tImages)

	// Проверка наличия целевых изображений с помощью обученных персептронов.
	println("Распознано (C): ${perceptron1.isTargetImage(PictureImage("test/c-test.jpg", true))}")
	println("Распознано (H): ${perceptron2.isTargetImage(PictureImage("test/h-test.jpg", true))}")
	println("Распознано (T): ${perceptron3.isTargetImage(PictureImage("test/t-test.jpg", true))}")
}