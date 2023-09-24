package hummel

fun main() {
	// Создание списков изображений для каждой категории: S, N, T.
	val tc1Images = ((0..3).map { PictureImage("train/0$it.jpg", 1) })
	val fh1Images = ((0..3).map { PictureImage("train/1$it.jpg", 0) })
	val ft1Images = ((0..3).map { PictureImage("train/2$it.jpg", 0) })
	val fc2Images = ((0..3).map { PictureImage("train/0$it.jpg", 0) })
	val th2Images = ((0..3).map { PictureImage("train/1$it.jpg", 1) })
	val ft2Images = ((0..3).map { PictureImage("train/2$it.jpg", 0) })
	val fc3Images = ((0..3).map { PictureImage("train/0$it.jpg", 0) })
	val fh3Images = ((0..3).map { PictureImage("train/1$it.jpg", 0) })
	val tt3Images = ((0..3).map { PictureImage("train/2$it.jpg", 1) })

	// Объединение списков изображений для каждой категории.
	val cImages = tc1Images + fh1Images + ft1Images
	val hImages = fc2Images + th2Images + ft2Images
	val tImages = fc3Images + fh3Images + tt3Images

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
	println("Распознано (C): ${perceptron1.isTargetImage(PictureImage("test/c-test.jpg", 1))}")
	println("Распознано (H): ${perceptron2.isTargetImage(PictureImage("test/h-test.jpg", 1))}")
	println("Распознано (T): ${perceptron3.isTargetImage(PictureImage("test/t-test.jpg", 1))}")
}