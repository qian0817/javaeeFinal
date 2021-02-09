group = "com.qianlei"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation("org.springframework.boot", "spring-boot-starter-actuator")
    implementation("org.springframework.boot", "spring-boot-starter-data-jpa")
    implementation("org.springframework.boot", "spring-boot-starter-data-redis")
    implementation("org.springframework.boot", "spring-boot-starter-security")
    implementation("org.springframework.boot", "spring-boot-starter-web")
    implementation("org.springframework.cloud", "spring-cloud-starter-circuitbreaker-resilience4j")
    implementation("org.springframework.cloud", "spring-cloud-starter-openfeign")
    implementation("org.springframework.cloud", "spring-cloud-starter-kubernetes-fabric8-all")
    implementation("org.springframework.cloud", "spring-cloud-starter-bootstrap")
    implementation("org.springframework.cloud", "spring-cloud-starter-sleuth")
    implementation("org.springframework.cloud", "spring-cloud-sleuth-zipkin")
    implementation("org.springframework.kafka", "spring-kafka")
    implementation("org.apache.commons", "commons-lang3")
    implementation("com.nimbusds", "nimbus-jose-jwt")
    implementation("org.springdoc", "springdoc-openapi-ui")
    implementation("cn.hutool", "hutool-all")
    implementation("org.jetbrains", "annotations")
    implementation("org.jsoup", "jsoup")
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
