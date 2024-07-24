pluginManagement {
	repositories {
		mavenLocal()
		mavenCentral()
		gradlePluginPortal()
	}
}

dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		mavenLocal()
		mavenCentral()
		gradlePluginPortal()
	}
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "latest.release"
}

include(":appLab1")
include(":appLab2")
include(":appLab3")
include(":appLab4")
include(":appLab5")
include(":appLab6")
include(":appLab7")
include(":appLab8")
include(":appLab9")