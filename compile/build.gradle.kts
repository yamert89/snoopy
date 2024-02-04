
plugins {
    id("java")
    id("maven-publish")
}

group = "yamert89.snoopy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    //testImplementation("org.junit.platform:junit-platform-suite-engine:1.10.1")
    //testImplementation("org.junit.platform:junit-platform-suite-api:1.10.1")
    testImplementation("ch.qos.logback:logback-classic:1.4.11")
    implementation(project(":meta"))
    implementation("org.ow2.asm:asm-util:9.5")
    implementation("org.ow2.asm:asm-tree:9.5")
    implementation("org.ow2.asm:asm-analysis:9.5")
    implementation("org.ow2.asm:asm-commons:9.5")
    implementation("org.slf4j:slf4j-api:2.0.9")

}

publishing.repositories.maven("../../_gradle-plugins-repository")

val jarName = "snoopy_compile.jar"

tasks{
    test {
        useJUnitPlatform()
        environment("snoopy.execPath", project.layout.buildDirectory.get().asFile.path)
        systemProperty("junit.jupiter.testclass.order.default", "org.junit.jupiter.api.ClassOrderer\$OrderAnnotation")
        //systemProperty("junit.jupiter.testinstance.lifecycle.default", "per_class")
    }

    compileJava {
        dependsOn(clean)
    }

    jar{
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        archiveFileName.set(jarName)

        from(arrayOf(
            configurations.compileClasspath.get().files.map{ if(it.isDirectory) it else zipTree(it)},
        ))
    }

    register("copyJar"){
        dependsOn("jar")
        delete("$rootDir/snoopy_gradle_plugin/libs/$jarName")
        copy{
            from("$buildDir/libs/$jarName")
            into("$rootDir/snoopy_gradle_plugin/libs/")
        }
    }
}