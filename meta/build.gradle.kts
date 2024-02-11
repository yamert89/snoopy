plugins {
    id("java")
    id("maven-publish")
}

group = "com.github.yamert89.snoopy"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

publishing {
    repositories {
        maven(project.ext.get("localRepository")!!)
        maven("https://jitpack.io")
    }
    publications {
        create<MavenPublication>("meta") {
            from(components["java"])
        }
    }
}

tasks.test {
    useJUnitPlatform()
}