import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.0"
}

repositories {
    mavenCentral()
}

dependencies {
    runtimeOnly(kotlin("reflect"))
    testImplementation(kotlin("test"))
    testImplementation(platform("org.junit:junit-bom:5.8.2"))
}

tasks {

    wrapper {
        gradleVersion = "7.3.1"
    }

    withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "11"
            freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
        }
    }

    test {
        useJUnitPlatform()
    }

}
