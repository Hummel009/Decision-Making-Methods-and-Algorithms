import java.time.LocalDate
import java.time.format.DateTimeFormatter

plugins {
	id("org.jetbrains.kotlin.jvm") version "1.9.20"
	id("application")
}

group = "org.example"
version = "v" + LocalDate.now().format(DateTimeFormatter.ofPattern("yy.MM.dd"))

repositories {
	mavenCentral()
}

val embed: Configuration by configurations.creating

dependencies {
	embed("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.20")
	embed("org.jfree:jfreechart:1.5.4")
	implementation("org.jfree:jfreechart:1.5.4")
}

application {
	mainClass = "hummel.MainKt"
}

tasks {
	jar {
		manifest {
			attributes(mapOf("Main-Class" to "hummel.MainKt"))
		}
		from(embed.map {
			if (it.isDirectory) it else zipTree(it)
		})
		duplicatesStrategy = DuplicatesStrategy.EXCLUDE
	}
}