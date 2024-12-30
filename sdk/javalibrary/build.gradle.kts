plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = JvmVersions.javaVersion
    targetCompatibility = JvmVersions.javaVersion
}
dependencies {
    ":sdk:jarlibrary".let {
        if (findProject(it) != null) {
            implementation(project(it))
        } else {
            implementation(Publish.getDependency(it))
        }
    }

    implementation(platform(libs.slf4j.bom))
    implementation(libs.slf4j)

    testImplementation(libs.slf4j.simple)
    testImplementation(libs.junit)
}