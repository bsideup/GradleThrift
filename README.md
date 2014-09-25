# Gradle plugin for Apache Thrift

The Thrift plugin provides Apache Thrift support for Gradle to your project.

## Usage
To use Thrift plugin, include in your build script:

```groovy
import org.gradle.internal.os.OperatingSystem

repositories {
    jcenter()

    ivy {
        artifactPattern "http://dl.bintray.com/bsideup/thirdparty/[artifact]-[revision](-[classifier]).[ext]"
    }
}

buildscript {
    repositories {
        jcenter()
    }

    dependencies {
        classpath "ru.trylogic.gradle.plugins:gradle-thrift-plugin:0.1.0"
    }
}

// Add Thrift plugin
apply plugin: 'thrift'
apply plugin: 'java'

// Define Thrift compile task. Default source dir is src/main/thrift, but you can override it.
task generateThrift(type : ru.trylogic.gradle.thrift.tasks.ThriftCompileTask) {
    generator = 'java:beans,hashcode'
    destinationDir = file("$buildDir/generated-sources/thrift/java")
}

// Make compileJava depends on Thrift task to generate sources before main compilation.
compileJava.dependsOn generateThrift

sourceSets {
    main {
        java {
            // Add generated Thrift files to the main java sources list.
            srcDir generateThrift.destinationDir
        }
    }
}

dependencies {
    Map platformMapping = [
            (OperatingSystem.WINDOWS) : 'win',
            (OperatingSystem.MAC_OS) : 'osx'
    ].withDefault { 'nix' }
    
    thrift "org.apache.thrift:thrift:0.9.0:${platformMapping.get(OperatingSystem.current())}@bin"
    
    compile 'org.apache.thrift:libthrift:0.9.0'
}
```
