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
snoopy{
    basePackage = "yamert89/snoopy/test_project"
    rootPath = layout.buildDirectory.get().asFile.path + "/classes/java/main"
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(project(":snoopy_compile"))
}

tasks{
    test {
        useJUnitPlatform()
    }
}