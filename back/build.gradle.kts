plugins {
    java
    id("org.springframework.boot") version "2.3.5.RELEASE"
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
    implementation("org.springframework.boot", "spring-boot-starter-actuator")
    implementation("org.springframework.boot", "spring-boot-starter-data-elasticsearch")
    implementation("org.springframework.boot", "spring-boot-starter-data-jpa")
    implementation("org.springframework.boot", "spring-boot-starter-data-redis")
    implementation("org.springframework.boot", "spring-boot-starter-mail")
    implementation("org.springframework.boot", "spring-boot-starter-security")
    implementation("org.springframework.boot", "spring-boot-starter-web")
    implementation("org.springframework.kafka", "spring-kafka")
    implementation("org.springframework.session", "spring-session-data-redis")
    implementation("org.redisson", "redisson-spring-boot-starter", "3.14.1")
    implementation("org.apache.commons", "commons-lang3")
    implementation("com.nimbusds", "nimbus-jose-jwt")
    implementation("org.springdoc", "springdoc-openapi-ui", "1.5.0")
    implementation("cn.hutool", "hutool-all", "5.5.1")
    implementation("org.jetbrains", "annotations", "20.1.0")
    implementation("org.jsoup", "jsoup", "1.13.1")
    compileOnly("org.projectlombok", "lombok")
    developmentOnly("org.springframework.boot", "spring-boot-devtools")
    testRuntimeOnly("com.h2database", "h2")
    runtimeOnly("mysql", "mysql-connector-java")
    annotationProcessor("org.springframework.boot", "spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok", "lombok")
    testImplementation("org.springframework.boot", "spring-boot-starter-test") {
        exclude("org.junit.vintage", "junit-vintage-engine")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
