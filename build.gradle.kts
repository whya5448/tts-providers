import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    `java-library`
    `maven-publish`
    kotlin("jvm") version "1.6.20"
}

group = "org.metalscraps.tts"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

val feignVersion = "11.8"
val slf4jVersion = "1.7.36"
val log4jVersion = "2.17.2"
val jacksonVersion = "2.13.2.2"
val jacksonModuleKotlinVersion = "2.13.2"

dependencies {
    // tts-core
    implementation("com.github.Whya5448:tts-core:9237347e65")

    // jackson
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonModuleKotlinVersion")

    // feign
    implementation("io.github.openfeign:feign-core:$feignVersion")
    implementation("io.github.openfeign:feign-slf4j:$feignVersion")
    implementation("io.github.openfeign:feign-jackson:$feignVersion")
    implementation("io.github.openfeign:feign-jaxb:$feignVersion")

    // jaxb
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("org.glassfish.jaxb:jaxb-runtime:2.4.0-b180830.0438")

    // logging
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    testImplementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    testImplementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4jVersion")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"

    val compileJava by tasks.compileJava
    destinationDirectory.set(compileJava.destinationDirectory)
}

java {
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
        }
    }
}