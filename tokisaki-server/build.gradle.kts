plugins {
    id("org.kordamp.gradle.jandex") version "1.0.0"
    kotlin("kapt")
}

val mapstructVersion = "1.5.5.Final"

dependencies {
    api(project(":tokisaki-core"))
    kapt("org.mapstruct:mapstruct-processor:$mapstructVersion")
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
}