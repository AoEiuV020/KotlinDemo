package com.aoeiuv020.javalibrary

import com.aoeiuv020.jarlibrary.JarLibraryClass
import org.slf4j.LoggerFactory

class JavaLibraryClass {
    companion object {
        val javaLibraryBean = JavaLibraryBean("JavaLibrary", "JavaLibraryBean")
        internal val logger by lazy { LoggerFactory.getLogger("JavaLibrary") }
    }

    private val jarLibraryClass = JarLibraryClass()

    fun init() {
        jarLibraryClass.init()
        logger.info("JavaLibraryClass init")
        logger.debug("JavaLibraryClass debug")
        logger.info("javaLibraryBean: {}", javaLibraryBean)
    }
}