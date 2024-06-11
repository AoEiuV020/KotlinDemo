package com.aoeiuv020.kotlindemo.util

inline fun <reified T : Any> T?.notNull(): T =
    notNull(type<T>().toString())

fun <T : Any> T?.notNull(value: String): T = requireNotNull(this) {
    "Required $value was null."
}