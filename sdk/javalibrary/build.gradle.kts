plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = JvmVersions.javaVersion
    targetCompatibility = JvmVersions.javaVersion
}
kotlin {
    jvmToolchain(JvmVersions.jvmIntTarget)
}
dependencies {
    if (findProject(":sdk:jarlibrary") != null) {
        implementation(project(":sdk:jarlibrary"))
    }

    implementation(platform(libs.slf4j.bom))
    implementation(libs.slf4j)

    testImplementation(libs.slf4j.simple)
    testImplementation(libs.junit)
}