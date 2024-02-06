rootProject.name = "snoopy"
include("gradle_plugin")
include("compile")
include("runtime")
include("meta")

pluginManagement {

    repositories {
        gradlePluginPortal()
        maven("../local_repository")
        maven("https://jitpack.io")
    }

    plugins {
        id("yamert89.snoopy") version "1.0"
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        //maven("https://jitpack.io")
        //maven("../local_repository")
    }
}
