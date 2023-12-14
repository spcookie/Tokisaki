pluginManagement {
    val quarkusPluginVersion: String by settings
    val quarkusPluginId: String by settings
    repositories {
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
    plugins {
        id(quarkusPluginId) version quarkusPluginVersion
    }
}
rootProject.name = "Tokisaki"

include("tokisaki-starter", "tokisaki-api", "tokisaki-core", "tokisaki-function", "tokisaki-server")