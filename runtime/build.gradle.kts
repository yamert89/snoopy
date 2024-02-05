plugins {
    id("java")
    id("maven-publish")
}

group = "com.github.yamert89.snoopy"
version = "0.1-SNAPSHOT"

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

tasks.test {
    useJUnitPlatform()
}