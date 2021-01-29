plugins {
    java
    id("org.springframework.boot") version "2.4.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

group = "com.qianlei"
version = "0.0.1-SNAPSHOT"

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.springframework.boot")

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }

    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    extra["springCloudAlibabaVersion"] = "2.2.2.RELEASE"
    extra["springCloudVersion"] = "2020.0.0"

    dependencyManagement {
        imports {
            mavenBom("com.alibaba.cloud:spring-cloud-alibaba-dependencies:${property("springCloudAlibabaVersion")}")
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
            dependencies {
                dependency("org.redisson:redisson-spring-boot-starter:3.14.1")
                dependency("org.springdoc:springdoc-openapi-ui:1.5.0")
                dependency("cn.hutool:hutool-all:5.5.1")
                dependency("org.jsoup:jsoup:1.13.1")
                dependency("org.jetbrains:annotations:20.1.0")
            }
        }
    }
}