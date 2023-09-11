package hummel

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

fun main() {
	Application.launch(Main::class.java)
}

class Main : Application() {
	override fun start(primaryStage: Stage) {
		val root = FXMLLoader.load<Parent>(Thread.currentThread().contextClassLoader.getResource("window.fxml"))
		primaryStage.title = "Syntactic method"
		primaryStage.scene = Scene(root, 800.0, 600.0)
		primaryStage.show()
	}
}