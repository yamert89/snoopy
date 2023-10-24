plugins {
    id("java")
    id("yamert89.snoopy")
}

repositories {
    mavenCentral()
    flatDir {
        dir("libs")
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks{
    test {
        useJUnitPlatform()
    }
}