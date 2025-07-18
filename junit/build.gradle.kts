// jacoco https://www.jacoco.org/jacoco/
// is a code coverage library for Java

plugins {
    kotlin("jvm") version "2.1.21"
    jacoco // https://docs.gradle.org/current/userguide/jacoco_plugin.html
}

group = "net.metsankulma"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

kotlin {
    jvmToolchain(17)
}