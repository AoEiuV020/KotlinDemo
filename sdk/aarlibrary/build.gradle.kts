plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.dokka)
    `maven-publish`
}

android {
    namespace = "com.aoeiuv020." + project.name
    compileSdk = AndroidVersions.compileSdk

    defaultConfig {
        minSdk = AndroidVersions.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JvmVersions.javaVersion
        targetCompatibility = JvmVersions.javaVersion
    }
    kotlinOptions {
        jvmTarget = JvmVersions.jvmTarget
    }
    publishing {
        singleVariant("release") {
            if (Publish.publishSourcesJar) {
                withSourcesJar()
            }
        }
    }
}

val dokkaJavadocJar by tasks.register<Jar>("dokkaJavadocJar") {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                artifactId = Publish.getArtifactId(project.path)
                from(components["release"])
                artifact(dokkaJavadocJar)
            }
        }
        repositories {
            maven {
                url = uri(rootDir.resolve("repo"))
            }
        }
    }
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}