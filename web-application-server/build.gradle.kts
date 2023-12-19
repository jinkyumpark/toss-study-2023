plugins {
    java
    kotlin("jvm") version "1.9.21"
}

group = "org.nhnnext"
version = "1.0"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

object Version {
    const val GUAVA = "18.0"
    const val LOGBACK = "1.1.2"
    const val JUNIT = "4.11"
}

dependencies {
    implementation("com.google.guava:guava:${Version.GUAVA}")
    implementation("ch.qos.logback:logback-classic:${Version.LOGBACK}")
    testImplementation("junit:junit:${Version.JUNIT}")
}

sourceSets {
    main {
        java.srcDir("src/main/kotlin")
        resources.srcDir("src/main/resources")
    }
    test {
        java.srcDir("src/test/java")
        resources.srcDir("target/test-classes")
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Copy> {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

tasks.jar {
    archiveBaseName.set("web-application-server")
}

kotlin {
    jvmToolchain(8)
}