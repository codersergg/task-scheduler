import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.0-SNAPSHOT"
    id("io.spring.dependency-management") version "1.1.3"
    id("org.graalvm.buildtools.native") version "0.9.27"
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.spring") version "1.9.10"
    kotlin("plugin.jpa") version "1.9.10"
    kotlin("plugin.allopen") version "1.9.10"
    kotlin("kapt") version "1.9.10"
}

group = "com.codersergg"
version = "0.0.1-SNAPSHOT"

buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("io.spring.gradle:dependency-management-plugin:1.1.3")
    }
}

dependencyManagement {
    dependencies {
        dependency("io.micrometer:micrometer-observation:1.11.4")
        dependency("io.micrometer:micrometer-commons:1.11.4")
    }
}

apply(plugin = "io.spring.dependency-management")

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // serialization
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // db, jpa
    //implementation("org.hibernate.orm:hibernate-core:6.3.0.Final")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.hibernate.validator:hibernate-validator:8.0.0.Final")
    implementation("org.glassfish:jakarta.el:4.0.2")
    implementation("org.hibernate.orm:hibernate-agroal:6.3.0.Final")
    implementation("io.agroal:agroal-pool:2.1")
    implementation("org.hibernate.orm:hibernate-jpamodelgen:6.3.0.Final")
    annotationProcessor("org.hibernate.orm:hibernate-jpamodelgen:6.3.0.Final")
    kapt("org.hibernate.orm:hibernate-jpamodelgen:6.3.0.Final")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")
    //implementation("org.flywaydb:flyway-core")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.mockk:mockk:1.13.8")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}