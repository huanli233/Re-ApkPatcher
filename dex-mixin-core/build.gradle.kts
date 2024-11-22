plugins {
    kotlin("jvm")
}

group = "com.huanli233"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(project(":dex-mixin-api"))
    implementation("com.android.tools.smali:smali-dexlib2:3.0.3")
    implementation("com.huanli233:multidexlib2:3.0.3.r2")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}