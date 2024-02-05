rootProject.name = "snoopy"
include("gradle_plugin")
include("compile")
include("runtime")
include("meta")

pluginManagement {

    repositories {
        gradlePluginPortal()
        maven("../_gradle-plugins-repository")
    }

    plugins {
        id("yamert89.snoopy") version "1.0"
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }
}
