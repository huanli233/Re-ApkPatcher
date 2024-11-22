plugins {
    java
}

group = "com.huanli233"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains:annotations:26.0.1")
}

tasks.test {
    useJUnitPlatform()
}