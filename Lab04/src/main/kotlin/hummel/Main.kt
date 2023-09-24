package hummel

fun main() {
	println("Reading images...")

	// Создание списков изображений для каждой категории: S, N, T.
	val s1Images = ((0..3).map { PictureImage("new/0$it.jpg", 1) })
	val n1Images = ((0..3).map { PictureImage("new/1$it.jpg", 0) })
	val t1Images = ((0..3).map { PictureImage("new/2$it.jpg", 0) })
	val s2Images = ((0..3).map { PictureImage("new/0$it.jpg", 0) })
	val n2Images = ((0..3).map { PictureImage("new/1$it.jpg", 1) })
	val t2Images = ((0..3).map { PictureImage("new/2$it.jpg", 0) })
	val s3Images = ((0..3).map { PictureImage("new/0$it.jpg", 0) })
	val n3Images = ((0..3).map { PictureImage("new/1$it.jpg", 0) })
	val t3Images = ((0..3).map { PictureImage("new/2$it.jpg", 1) })

	// Объединение списков изображений для каждой категории.
	val sImages = s1Images + n1Images + t1Images
	val nImages = s2Images + n2Images + t2Images
	val tImages = s3Images + n3Images + t3Images

	// Создание трех персептронов для каждой категории.
	val perceptron1 = Perceptron(imagePixelCount = 36, iterationCount = 10000000)
	val perceptron2 = Perceptron(imagePixelCount = 36, iterationCount = 10000000)
	val perceptron3 = Perceptron(imagePixelCount = 36, iterationCount = 10000000)

	println("Perceptron 1 training...")
	// Обучение первого персептрона на изображениях из категории S.
	perceptron1.train(sImages)

	println("Perceptron 2 training...")
	// Обучение второго персептрона на изображениях из категории N.
	perceptron2.train(nImages)

	println("Perceptron 3 training...")
	// Обучение третьего персептрона на изображениях из категории T.
	perceptron3.train(tImages)

	// Проверка наличия целевых изображений с помощью обученных персептронов.
	println("Is positive test passed (S): ${perceptron1.isTargetImage(PictureImage("new/s-test.jpg", 1))}")
	println("Is positive test passed (N): ${perceptron2.isTargetImage(PictureImage("new/n-test.jpg", 1))}")
	println("Is positive test passed (T): ${perceptron3.isTargetImage(PictureImage("new/t-test.jpg", 1))}")
}