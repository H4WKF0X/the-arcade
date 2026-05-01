plugins {
    id("java")
    id("application")
}

group = "com.mpaesold.arcade"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jline:jline:3.26.3")
    implementation("net.java.dev.jna:jna:5.14.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.reflections:reflections:0.10.2")
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    runtimeOnly("org.slf4j:slf4j-nop:2.0.17")
}

application {
    mainClass.set("core.launcher.Main")
}

tasks.named<JavaExec>("run") {
    workingDir = projectDir
    standardInput = System.`in`
}

// Kept for IDE / WSL use where stdin is a real TTY
tasks.register<JavaExec>("menuDemo") {
    group = "demo"
    mainClass.set("core.tui.MenuDemo")
    classpath = sourceSets["main"].runtimeClasspath
    workingDir = projectDir
    standardInput = System.`in`
}


tasks.test {
    useJUnitPlatform()
}