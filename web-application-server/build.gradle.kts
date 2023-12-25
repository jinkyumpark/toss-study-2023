plugins {
    java
    kotlin("jvm") version "1.9.21"
}

group = "com.jinkyumpark"
version = "1.0"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

object Version {
    const val LOGBACK = "1.1.2"
    const val JUNIT = "4.11"
    const val JAVA = "17"
}

dependencies {
    implementation("ch.qos.logback:logback-classic:${Version.LOGBACK}")
    testImplementation("junit:junit:${Version.JUNIT}")
}

sourceSets {
    main {
        java.srcDir("src/main/kotlin")
        resources.srcDir("src/main/resources")
    }
    test {
        java.srcDir("src/test/kotlin")
        resources.srcDir("target/test-classes")
    }
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    copy {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    jar {
        archiveBaseName.set("web-application-server")
    }
}

kotlin {
    jvmToolchain(Version.JAVA.toInt())
}
