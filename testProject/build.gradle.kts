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
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(project(":compile"))
    implementation(project(":runtime"))
}

tasks{
    test {
        useJUnitPlatform()
    }
    snoopyCompile {
        mustRunAfter(classes)
        doFirst { println("snoopy started...") }
    }
    jar {
        dependsOn(snoopyCompile)
    }

}