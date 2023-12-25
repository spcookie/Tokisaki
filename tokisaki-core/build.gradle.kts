plugins {
    id("org.kordamp.gradle.jandex") version "1.0.0"
    kotlin("plugin.serialization") version "1.9.10"
}

dependencies {
    api("cn.hutool:hutool-core:5.8.16")
    implementation("io.github.spcookie:mirai-core-jvm:2.16.0-hook")
}