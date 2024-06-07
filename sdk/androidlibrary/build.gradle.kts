plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
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
}

dependencies {
    implementation(project(":sdk:javalibrary"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}