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

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

gradlePlugin {
    plugins {
        create("sgp") {
            id = "yamert89.snoopy"
            implementationClass = "yamert89.snoopy.SnoopyPlugin"
        }
    }
}
publishing {
    repositories {
        maven(project.ext.get("localRepository")!!)
        maven("https://jitpack.io")
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
    jar{
        dependsOn(":compile:jar")
        from(configurations.compileClasspath.get().files
            .filter { it.name == "snoopy_compile.jar" }
            .map{ if(it.isDirectory) it else zipTree(it)})
    }
}
