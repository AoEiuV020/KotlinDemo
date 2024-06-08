package com.aoeiuv020.jarlibrary

import com.aoeiuv020.jarlibrary.JarLibraryClass.Companion.jarLibraryBean

import org.junit.Test
import org.slf4j.simple.SimpleLogger

class JarLibraryClassTest {
    init {
        System.setProperty(SimpleLogger.LOG_KEY_PREFIX + "JarLibrary", "trace")
    }

    @Test
    fun init() {
        val jarLibraryClass = JarLibraryClass()
        jarLibraryClass.init()
        println(jarLibraryBean)
    }
}