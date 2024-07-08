plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

println("from build-src build script: ${libs.versions.bb.get()}")

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

allprojects {
    project.layout.buildDirectory.set(rootDir.resolve("../build")
        .resolve(rootProject.name)
        .resolve(project.path.replace(":", ".")))
}
