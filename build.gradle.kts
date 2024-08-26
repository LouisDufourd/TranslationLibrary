plugins {
    id("java")
}

group = "com.plaglefleau"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("org.jetbrains:annotations:24.1.0")
}

tasks.test {
    useJUnitPlatform()
}