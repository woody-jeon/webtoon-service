tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    implementation(project(":core:core-domain"))
    implementation(project(":core:core-enum"))
    implementation(project(":support:logging"))

    runtimeOnly(project(":storage:db-core"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
}
