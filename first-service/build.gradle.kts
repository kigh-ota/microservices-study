plugins {
    kotlin("jvm") version "1.5.10"
}

val exposedVersion: String by project

dependencies {
    implementation("io.javalin:javalin:3.13.10")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("org.apache.kafka:kafka-clients:2.8.0")
    implementation("mysql:mysql-connector-java:8.0.26")
    implementation("ch.qos.logback:logback-classic:1.2.3")
}
