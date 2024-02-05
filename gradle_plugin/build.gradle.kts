plugins {
    id("java")
    id("java-gradle-plugin")
    id("maven-publish")
}

version = "1.0"
group = "com.github.yamert89.snoopy"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("ch.qos.logback:logback-classic:1.4.11")
    compileOnly(project(":compile"))
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
    test {
        useJUnitPlatform()
    }
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
