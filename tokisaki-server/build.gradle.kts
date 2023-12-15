plugins {
    id("org.kordamp.gradle.jandex") version "1.0.0"
    kotlin("kapt")
}

val mapstructVersion = "1.5.5.Final"

dependencies {
    implementation(project(":tokisaki-core"))
    implementation(project(":tokisaki-function"))
    kapt("org.mapstruct:mapstruct-processor:$mapstructVersion")
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
}