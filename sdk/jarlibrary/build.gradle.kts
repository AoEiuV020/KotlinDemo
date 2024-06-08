plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    `maven-publish`
}
java {
    withJavadocJar()
    if (Publish.publishSourcesJar) {
        withSourcesJar()
    }
}
publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = Publish.getArtifactId(project.path)
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri(rootDir.resolve("repo"))
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