plugins {
    id("org.kordamp.gradle.jandex") version "1.0.0"
    kotlin("plugin.serialization") version "1.9.10"
}

dependencies {
    api("cn.hutool:hutool-core:5.8.16")
//    implementation("net.mamoe:mirai-core:2.16.0")
    implementation(fileTree("libs") { include("*.jar") })
    implementation("net.mamoe:mirai-logging-slf4j:2.16.0") {
        exclude("net.mamoe", "mirai-console-compiler-annotation-jvm")
        exclude("net.mamoe", "mirai-core-api-jvm")
        exclude("net.mamoe", "mirai-core-utils-jvm")
    }
}