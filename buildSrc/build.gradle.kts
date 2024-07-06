plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}
allprojects {
    project.layout.buildDirectory.set(rootDir.resolve("../build")
        .resolve(rootProject.name)
        .resolve(project.path.replace(":", ".")))
}
