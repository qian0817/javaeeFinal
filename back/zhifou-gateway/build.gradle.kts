group = "com.qianlei"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation("org.springframework.boot", "spring-boot-starter-actuator")
    implementation("org.springframework.cloud", "spring-cloud-starter-gateway")
    implementation("org.springframework.cloud", "spring-cloud-starter-consul-all")
    annotationProcessor("org.springframework.boot", "spring-boot-configuration-processor")
    developmentOnly("org.springframework.boot", "spring-boot-devtools")
    testImplementation("org.springframework.boot", "spring-boot-starter-test")
}
