allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

dependencies {
    compileOnly(project(":core:core-domain"))
    implementation(project(":core:core-enum"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("com.querydsl:querydsl-jpa:${property("querydslVersion")}:jakarta")
    kapt("com.querydsl:querydsl-apt:${property("querydslVersion")}:jakarta")
    kapt("jakarta.annotation:jakarta.annotation-api")
    kapt("jakarta.persistence:jakarta.persistence-api")

    implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.0")

    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("com.h2database:h2")
}
