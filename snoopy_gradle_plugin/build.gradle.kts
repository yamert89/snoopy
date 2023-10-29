plugins {
    id("java")
    id("java-gradle-plugin")
    id("maven-publish")
}

version = "1.0"
group = "yamert89.snoopy"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    compileOnly(project(":snoopy_compile"))
}

gradlePlugin {
    plugins {
        create("sgp") {
            id = "yamert89.snoopy"
            implementationClass = "yamert89.snoopy.SnoopyPlugin"
        }
    }
}
publishing.repositories.maven("../../_gradle-plugins-repository")

tasks {
    jar{
       // duplicatesStrategy = DuplicatesStrategy.WARN

        from(configurations.compileClasspath.get().files
            .filter { it.name == "snoopy_compile.jar" }
            .map{ if(it.isDirectory) it else zipTree(it)})
        //dependsOn(":snoopy_compile:copyJar")

        /*from("libs/")
        manifest{
            attributes("Class-Path" to "snoopy_compile.jar")
        }*/
    }
}
