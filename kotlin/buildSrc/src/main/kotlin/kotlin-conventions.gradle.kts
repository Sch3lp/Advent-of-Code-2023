import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm")
    `java-library`
}

repositories {
    mavenCentral()
}

val kotestVersion = "5.8.0"

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.7.3"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        languageVersion = "1.9"
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}