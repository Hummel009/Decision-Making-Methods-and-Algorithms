import java.time.LocalDate
import java.time.format.DateTimeFormatter

plugins {
	id("org.jetbrains.kotlin.jvm") version "1.9.21"
	id("org.openjfx.javafxplugin") version "0.1.0"
	id("application")
}

group = "org.example"
version = "v" + LocalDate.now().format(DateTimeFormatter.ofPattern("yy.MM.dd"))

repositories {
	mavenCentral()
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

application {
	mainClass = "hummel.MainKt"
}

javafx {
	version = "22-ea+11"
	modules = listOf("javafx.controls", "javafx.fxml")
}

tasks {
	named<JavaExec>("run") {
		standardInput = System.`in`
	}
	jar {
		manifest {
			attributes(
				mapOf(
					"Main-Class" to "hummel.MainKt"
				)
			)
		}
		from(configurations.runtimeClasspath.get().map {
			if (it.isDirectory) it else zipTree(it)
		})
		duplicatesStrategy = DuplicatesStrategy.EXCLUDE
	}
}
