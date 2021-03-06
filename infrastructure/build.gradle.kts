import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.0"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("jvm") version "1.4.10"
    `java-library`
}
group = "com.siyamand.aws.dynamodb"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("software.amazon.awssdk:bom:2.15.44"))

    implementation("software.amazon.awssdk:dynamodb")
    implementation("software.amazon.awssdk:sts")
    implementation("software.amazon.awssdk:lambda")
    implementation("software.amazon.awssdk:iam")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.4.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.21")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.4.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.mockk:mockk:1.10.3")
    testImplementation("org.jetbrains.kotlin:kotlin-reflect:1.4.10")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.4.10")
    testImplementation("io.projectreactor:reactor-test:3.4.0")
    implementation("com.siyamand.aws.dynamodb:core:1.0.0")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}
