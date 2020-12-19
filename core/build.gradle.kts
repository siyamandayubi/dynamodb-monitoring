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
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.21")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("io.projectreactor:reactor-core:3.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.4.2")
    testImplementation("io.projectreactor:reactor-test:3.4.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}
tasks.withType<Test>(){
    useJUnitPlatform()
}
