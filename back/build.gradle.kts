plugins {
    java
    id("org.springframework.boot") version "2.3.4.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
}

group = "com.qianlei"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("cn.authing:java-core:3.0.1")
    implementation("com.squareup.okhttp3:okhttp:4.8.0")
    implementation("com.nimbusds:nimbus-jose-jwt:9.1.1")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testRuntimeOnly("com.h2database:h2")
    runtimeOnly("mysql:mysql-connector-java")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
