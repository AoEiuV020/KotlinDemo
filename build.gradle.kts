// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}
allprojects {
    println("adding project: " + project.path)
    project.layout.buildDirectory.set(rootProject.file("build").resolve(project.path.replace(":", ".")))
}
