plugins {
    id("org.kordamp.gradle.jandex") version "1.0.0"
    kotlin("plugin.serialization") version "1.9.10"
    kotlin("kapt")
}

val mapstructVersion = "1.5.5.Final"

dependencies {
    implementation(project(":tokisaki-core"))
    implementation("io.quarkiverse.minio:quarkus-minio:3.3.1")
    implementation("com.vdurmont:emoji-java:5.1.1")
    kapt("org.mapstruct:mapstruct-processor:$mapstructVersion")
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
}