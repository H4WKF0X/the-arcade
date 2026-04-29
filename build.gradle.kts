plugins {
    id("java")
}

group = "com.mpaesold.arcade"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jline:jline:3.26.3")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.reflections:reflections:0.10.2")
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}