package com.aoeiuv020.javalibrary

import com.aoeiuv020.javalibrary.JavaLibraryClass.Companion.javaLibraryBean
import org.junit.Assert.*

import org.junit.Test
import org.slf4j.simple.SimpleLogger

class JavaLibraryClassTest {
    init {
        System.setProperty(SimpleLogger.LOG_KEY_PREFIX + "JavaLibrary", "trace")
    }
    @Test
    fun init() {
        val javaLibraryClass = JavaLibraryClass()
        javaLibraryClass.init()
        println(javaLibraryBean)
    }
}