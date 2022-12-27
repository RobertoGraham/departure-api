import info.solidsoft.gradle.pitest.PitestPluginExtension

plugins {
    java
    groovy
    id("org.springframework.boot") version "3.0.1"
    id("io.spring.dependency-management") version "1.1.0"
    id("info.solidsoft.pitest") version "1.9.11"
}

group = "io.github.robertograham"
version = "0.0.1-SNAPSHOT"
description = "Departure API"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(19))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.apache.commons:commons-lang3")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation(platform("org.spockframework:spock-bom:2.3-groovy-4.0"))
    testImplementation("org.spockframework:spock-core")
    testImplementation("org.spockframework:spock-spring")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

configure<PitestPluginExtension> {
    junit5PluginVersion.set("1.1.0")
    threads.set(Runtime.getRuntime().availableProcessors())
    outputFormats.set(setOf("HTML"))
    mutators.set(setOf("STRONGER"))
    timestampedReports.set(false)
}
