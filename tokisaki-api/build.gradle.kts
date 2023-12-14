plugins {
    id("org.kordamp.gradle.jandex") version "1.0.0"
    kotlin("plugin.serialization") version "1.9.10"
}

dependencies {
    implementation(project(":tokisaki-server"))
}