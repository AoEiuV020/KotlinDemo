gradle.extra["module.jarlibraryDebug"] = false
extra["module.jarlibrary"]?.takeIf { it is String && File(it).isDirectory }?.let {
    gradle.extra["module.jarlibraryDebug"] = true
    include(":sdk:jarlibrary")
}
