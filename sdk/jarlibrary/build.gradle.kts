plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.dokka)
    `maven-publish`
}
java {
    if (Publish.publishSourcesJar) {
        withSourcesJar()
    }
}

val dokkaJavadocJar by tasks.register<Jar>("dokkaJavadocJar") {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = Publish.getArtifactId(project.path)
            from(components["java"])
            artifact(dokkaJavadocJar)
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
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget(JvmVersions.kotlinJvmTarget)
    }
}
dependencies {
    implementation(platform(libs.slf4j.bom))
    implementation(libs.slf4j)

    testImplementation(libs.slf4j.simple)
    testImplementation(libs.junit)
}