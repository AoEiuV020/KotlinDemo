plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    `maven-publish`
}
publishing {
    publications {
        create<MavenPublication>("maven")
    }
    repositories {
        maven {
            url = uri(rootProject.file("repo"))
        }
    }
}
java {
    sourceCompatibility = JvmVersions.javaVersion
    targetCompatibility = JvmVersions.javaVersion
}
kotlin {
    jvmToolchain(JvmVersions.jvmIntTarget)
}
dependencies {
    implementation(platform(libs.slf4j.bom))
    implementation(libs.slf4j)

    testImplementation(libs.slf4j.simple)
    testImplementation(libs.junit)
}