plugins {
    id("org.kordamp.gradle.jandex") version "1.0.0"
    kotlin("kapt")
}

val mapstructVersion = "1.5.5.Final"
val jpamodelgenVersion = "6.2.13.Final"

dependencies {
    implementation(project(":tokisaki-core"))
    implementation(project(":tokisaki-function"))
    kapt("org.mapstruct:mapstruct-processor:$mapstructVersion")
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    kapt("org.hibernate:hibernate-jpamodelgen:$jpamodelgenVersion")
    compileOnly("org.hibernate:hibernate-jpamodelgen:$jpamodelgenVersion")
}