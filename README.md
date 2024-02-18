# snoopy

Roughly speaking, this is a simple tool for injecting sql strings into java classes at the
compilation stage.

> Note! This should not be considered as a production-ready tool. It's more like experiment

**snoopy** has several modules:

- **"compile"** is a core implementation, can be used as the standalone library
- **"gradle_plugin"** is a core implementation wrapped as plugin
- **"meta"** contains public annotations

### How it works?

This tool uses the ASM library to manipulate bytecode. The corresponding Gradle task should execute it after general
compilation.

### How to use it in source code?

- add the file with the name **SQL1.sql** to **/resources**
- add the class-level annotation

        @InjectSQL(fieldsStartWith = "SQL")
        public class ReplaceSQLExample {
            public String SQL1;
            public final String regularField = "regularField";
        }

or add field-level annotation. It has the highest priority.

          public class ReplaceSQLFieldExample {
              @InjectSQLField(name = "SQL1")
              public String SQL;
              public final String regularField = "regularField";
          }

- Additionally, you can add a custom filter

        @InjectSQL(fieldsStartWith = "SQL", filter = TestFilter2.class)
        public class Filters {
            @InjectSQLField(name = "SQL6", filter = TestFilter.class)
            String SQL;
            String SQL2;
        }

Field-level filter has the highest priority.

### How to use it in the Gradle?

The plugin produces the task named **snoopyCompile** and extension named **snoopy**.
Your **build.gradle.kts** will look like this:

    plugins {
        id("java")
        id("com.github.yamert89.snoopy")
    }

    dependencies {
        implementation("com.github.yamert89.snoopy:meta:1.0.0")
    }
    tasks{
        snoopyCompile {
            dependsOn(classes)
        }
    
        create<JavaExec>("runApp") {
          classpath = sourceSets.main.get().runtimeClasspath
          mainClass = "user.package.Main"
          dependsOn(snoopyCompile)
        }
    }

    snoopy {
        classDir = "/path/to/classes" // optionally, "<YOUR BUILD DIRECTORY>/classes/java/main" by default
        resourcesDir = "/path/to/resources" // // optionally, "<YOUR BUILD DIRECTORY>/resources/main" by default
    }

> Note!
> **classDir** and **resourcesDir** are relative paths from build directory.

**runApp** task needs for running compiled code from IDE.

**settings.gradle.kts** will look like this:

    pluginManagement {
        repositories {
            gradlePluginPortal()
            maven("https://jitpack.io")
        }
        plugins {
            id("com.github.yamert89.snoopy") version "1.0.0"
        }
    }
    
    dependencyResolutionManagement {
        repositories {
            mavenCentral()
            maven{
                url = uri("https://jitpack.io")
            }
        }
    }

### Current limitations

- current release made for the JVM 17 or later. (Gradle 7.3 or later should be used)
- do not use replaced fields from other classes, only from getters.
- single resource file must contain single sql query

All of these limitations may be fixed in the future.

Check how this work with an example: https://github.com/yamert89/snoopyExample
