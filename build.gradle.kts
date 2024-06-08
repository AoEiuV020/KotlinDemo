// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}
System.setProperty("maven.repo.local", rootProject.file("local").absolutePath)
allprojects {
    project.layout.buildDirectory.set(rootProject.file("build").resolve(project.path.replace(":", ".")))
    group = Publish.groupId
    version = System.getenv("BUILD_VERSION") ?: Publish.version
}
