plugins {
    id("java")
}

group = "yamert89.snoopy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    flatDir {
        dir("libs")
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("yamert89.snoopy:snoopy_compile")
}

tasks.test {
    useJUnitPlatform()
}