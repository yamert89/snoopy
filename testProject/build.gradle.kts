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

}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(project(":compile"))
    implementation(project(":runtime"))
    implementation(project(":meta"))
}

tasks{
    test {
        useJUnitPlatform()
    }
    snoopyCompile {
        dependsOn(classes)
        doFirst { println("snoopy started...") }
    }

    create<JavaExec>("runApp") {
        classpath = sourceSets.main.get().runtimeClasspath
        mainClass = "yamert89.snoopy.test_project.Main"
        dependsOn(snoopyCompile)
    }

}