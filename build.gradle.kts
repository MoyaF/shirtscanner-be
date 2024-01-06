import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.9.22"
    val springBootVersion = "3.2.1"
    val dependencyManagementVersion = "1.1.4"
    val testLoggerVersion = "4.0.0"

    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    id("org.springframework.boot") version springBootVersion
    id("io.spring.dependency-management") version dependencyManagementVersion
    id("com.adarshr.test-logger") version testLoggerVersion
}

group = "io.moya"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

val jsoupVersion = "1.17.2"
val kotlinLoggingVersion = "3.0.5"
val wiremockVersion = "3.3.1"

dependencies {
    //Spring
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    //Jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    //Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    //Logging
    implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")

    //Scraping
    implementation("org.jsoup:jsoup:$jsoupVersion")

    //Cache
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.github.ben-manes.caffeine:caffeine")

    //Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.wiremock:wiremock-standalone:$wiremockVersion")
    testImplementation("io.rest-assured:kotlin-extensions:5.4.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }

}

tasks.withType<Test> {
    useJUnitPlatform()
    maxParallelForks = 8
}