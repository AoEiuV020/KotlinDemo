package com.aoeiuv020.kotlindemo

import com.aoeiuv020.androidlibrary.AndroidLibraryClass
import org.junit.Test
import org.slf4j.simple.SimpleLogger

class AndroidLibraryClassTest {
    init {
        System.setProperty(SimpleLogger.LOG_KEY_PREFIX + "JavaLibrary", "trace")
        System.setProperty(SimpleLogger.LOG_KEY_PREFIX + "AndroidLibrary", "trace")
    }

    @Test
    fun init() {
        val androidLibraryClass = AndroidLibraryClass()
        androidLibraryClass.init()
        println(AndroidLibraryClass.androidLibraryBean)
        println(AndroidLibraryClass.javaLibraryBean)
    }
}