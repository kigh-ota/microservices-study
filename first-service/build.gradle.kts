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
    implementation("mysql:mysql-connector-java:8.0.26")
    implementation("org.slf4j:slf4j-simple:1.7.32")
}
