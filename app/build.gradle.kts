plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.aoeiuv020.kotlindemo"
    compileSdk = AndroidVersions.compileSdk

    defaultConfig {
        applicationId = "com.aoeiuv020.kotlindemo"
        minSdk = AndroidVersions.minSdk
        targetSdk = AndroidVersions.targetSdk
        versionCode = AndroidVersions.versionCode
        versionName = AndroidVersions.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        // 从properties读取字段插入到BuildConfig，
        // 依赖props.gradle.kts脚本读取properties到extra，
        // 不过滤空的值，保留空字符串，
        // 编译时的任务generateDebugBuildConfig之后生效，
        gradle.extra.properties.filter {
            it.key.startsWith("field.") && it.value is String
        }.mapValues {
            it.value as String
        }.mapKeys {
            it.key.removePrefix("field.")
        }.forEach { (key, value) ->
            println("BuildConfig.$key = \"$value\"")
            buildConfigField("String", key, "\"" + value + "\"")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
// pc端的单元测试移除无法使用的slf4j-android，
// 关键是runtimeOnly依赖不只加入apk中，test也会加上，
configurations
    .filter { it.name.startsWith("test") }
    .forEach { conf ->
        conf.exclude(module = "slf4j-android")
    }
dependencies {
    implementation(project(":sdk:androidlibrary"))
    implementation(platform(libs.slf4j.bom))
    implementation(libs.slf4j)
    runtimeOnly(libs.slf4j.android)

    testImplementation(libs.slf4j.simple)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

apply(rootDir.resolve("gradle/signing.gradle"))
