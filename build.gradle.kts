plugins {
    java
    id("io.qameta.allure") version "2.12.0"
}

group = "com.shop"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

val restAssuredVersion = "5.5.0"
val junitVersion = "5.11.3"
val allureVersion = "2.29.1"
val jacksonVersion = "2.18.1"
val selenideVersion = "7.7.3"

dependencies {
    // REST Assured
    implementation("io.rest-assured:rest-assured:$restAssuredVersion")
    implementation("io.rest-assured:json-schema-validator:$restAssuredVersion")

    // Selenide (UI testing)
    implementation("com.codeborne:selenide:$selenideVersion")

    // Jackson
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    // Owner (configuration)
    implementation("org.aeonbits.owner:owner:1.0.12")

    // Faker (test data generation)
    implementation("net.datafaker:datafaker:2.4.2")

    // AssertJ
    implementation("org.assertj:assertj-core:3.26.3")

    // Allure
    implementation("io.qameta.allure:allure-rest-assured:$allureVersion")
    implementation("io.qameta.allure:allure-selenide:$allureVersion")
    testImplementation("io.qameta.allure:allure-junit5:$allureVersion")

    // JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

tasks.test {
    useJUnitPlatform()
    jvmArgs = listOf("-Dfile.encoding=UTF-8")
}
