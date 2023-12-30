plugins {
    id("org.kordamp.gradle.jandex") version "1.0.0"
    kotlin("plugin.serialization") version "1.9.10"
    kotlin("kapt")
}

dependencies {
    api("cn.hutool:hutool-core:5.8.16")
    implementation("io.github.spcookie:mirai-core-jvm:2.16.0")
    implementation("net.mamoe:mirai-logging-slf4j:2.16.0") {
        exclude("net.mamoe", "mirai-compiler-annotations-jvm")
        exclude("net.mamoe", "mirai-core-api-jvm")
        exclude("net.mamoe", "mirai-core-jvm")
        exclude("net.mamoe", "mirai-core-utils-jvm")
    }
}