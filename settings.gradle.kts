rootProject.name = "snoopy"
include("snoopy_gradle_plugin")
include("snoopy_compile")
include("testProject")
include("snoopy_runtime")

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

