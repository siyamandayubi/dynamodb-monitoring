import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.0"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("jvm") version "1.4.21"
    kotlin("plugin.serialization") version "1.4.21"
    `java-library`
}
group = "com.siyamand.aws.dynamodb"
version = "1.0.0"

repositories {
    mavenCentral()
}
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")

    implementation("org.freemarker:freemarker:2.3.30")

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.21")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("io.projectreactor:reactor-core:3.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.4.2")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.4.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.mockk:mockk:1.10.3")
    testImplementation("org.jetbrains.kotlin:kotlin-reflect:1.4.10")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.4.10")
    testImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    testImplementation("io.projectreactor:reactor-test:3.4.0")}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}
