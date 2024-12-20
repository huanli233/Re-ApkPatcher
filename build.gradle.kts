plugins {
    kotlin("jvm") version "2.0.10"
}

group = "com.huanli233"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("com.beust:jcommander:1.82")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}