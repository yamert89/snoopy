rootProject.name = "snoopy"
include("gradle_plugin")
include("compile")
include("runtime")

pluginManagement {
    repositories.gradlePluginPortal()

    repositories.maven("../_gradle-plugins-repository")
    plugins {
        id("yamert89.snoopy") version "1.0"
    }
}

dependencyResolutionManagement {
    repositories.mavenCentral()
}
include("meta")
