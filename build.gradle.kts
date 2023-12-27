import io.quarkus.gradle.tasks.QuarkusDev

plugins {
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.allopen") version "1.9.10"
    id("io.quarkus")
}

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
    }
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

subprojects {

    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.allopen")
        plugin("io.quarkus")
    }

    dependencies {
        implementation(platform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
        implementation("io.quarkus:quarkus-smallrye-openapi")
        implementation("io.quarkus:quarkus-resteasy-reactive-jaxb")
        implementation("io.quarkus:quarkus-rest-client-reactive-kotlin-serialization")
        implementation("io.quarkus:quarkus-resteasy-reactive")
        implementation("io.quarkus:quarkus-config-yaml")
        implementation("io.quarkus:quarkus-smallrye-fault-tolerance")
        implementation("io.quarkus:quarkus-scheduler")
        implementation("io.quarkus:quarkus-smallrye-jwt")
        implementation("io.quarkus:quarkus-resteasy-reactive-kotlin-serialization")
        implementation("io.quarkus:quarkus-narayana-jta")
        implementation("io.quarkus:quarkus-hibernate-validator")
        implementation("io.quarkus:quarkus-quartz")
        implementation("io.quarkus:quarkus-kotlin")
        implementation("io.quarkus:quarkus-hibernate-reactive-panache-kotlin")
        implementation("io.quarkus:quarkus-redis-client")
        implementation("io.quarkus:quarkus-reactive-pg-client")
        implementation("io.quarkus:quarkus-redis-cache")
        implementation("io.quarkus:quarkus-smallrye-jwt")
        implementation("io.quarkus:quarkus-smallrye-jwt-build")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("io.smallrye.reactive:mutiny-kotlin")
        implementation("io.quarkus:quarkus-arc")
        testImplementation("io.quarkus:quarkus-junit5")
        testImplementation("io.rest-assured:rest-assured")
    }

    group = "io.micro"
    version = "1.0.0"

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<QuarkusDev> {
        jvmArgs = listOf("-Dfile.encoding=UTF-8")
    }

    tasks.withType<Test> {
        systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
    }

    allOpen {
        annotation("jakarta.ws.rs.Path")
        annotation("jakarta.enterprise.context.ApplicationScoped")
        annotation("jakarta.enterprise.context.Singleton")
        annotation("jakarta.persistence.Entity")
        annotation("io.quarkus.test.junit.QuarkusTest")
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
        kotlinOptions.javaParameters = true
    }
}