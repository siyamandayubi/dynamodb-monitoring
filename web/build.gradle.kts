import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    "java"
    id("org.springframework.boot") version "2.4.0"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("jvm") version "1.4.21"
}
group = "com.siyamand.aws.dynamodb"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-quartz")
    implementation("com.siyamand.aws.dynamodb:core:1.0.0")
    implementation("com.siyamand.aws.dynamodb:infrastructure:1.0.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.4.2")
    implementation("io.jsonwebtoken:jjwt-api:0.11.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.21")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.2")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test>(){
    useJUnitPlatform()
}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}
