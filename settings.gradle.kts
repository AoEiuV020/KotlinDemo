pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
        maven {
            url = uri(rootDir.resolve("repo"))
        }
        google()
        mavenCentral()
    }
}

rootProject.name = "KotlinDemo"
include(":app")
include(":sdk:androidlibrary")
include(":sdk:javalibrary")

apply("./gradle/props.gradle.kts")
apply("./gradle/project.gradle.kts")

extra.properties.toSortedMap().forEach { (key, value) ->
    println("$key => $value")
}
