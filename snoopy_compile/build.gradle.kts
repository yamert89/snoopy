import org.gradle.jvm.tasks.Jar


plugins {
    id("java")
}

group = "yamert89.snoopy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.ow2.asm:asm-util:9.5")
    implementation("org.ow2.asm:asm-tree:9.5")
    implementation("org.ow2.asm:asm-analysis:9.5")
    implementation("org.ow2.asm:asm-commons:9.5")
}

tasks.test {
    useJUnitPlatform()
}

val jarName = "snoopy_compile.jar"

tasks{
    jar{
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        archiveFileName.set(jarName)

        from(arrayOf(
            //configurations.runtimeClasspath.get().files.map{ if(it.isDirectory) it else zipTree(it)},
            configurations.compileClasspath.get().files.map{ if(it.isDirectory) it else zipTree(it)},
        ))
    }

    register("copyJar"){
        dependsOn("jar")
        println("$rootDir/testProject/libs/$jarName")
        copy{
            from("$buildDir/libs/$jarName")
            into("$rootDir/testProject/libs/")
        }
    }
}