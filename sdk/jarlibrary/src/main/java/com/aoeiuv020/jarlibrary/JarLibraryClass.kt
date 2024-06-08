package com.aoeiuv020.jarlibrary

import org.slf4j.LoggerFactory

class JarLibraryClass {
    companion object {
        val jarLibraryBean = JarLibraryBean("JarLibrary", "JarLibraryBean")
        internal val logger by lazy { LoggerFactory.getLogger("JarLibrary") }
    }

    fun init() {
        logger.info("JarLibraryClass init")
        logger.debug("JarLibraryClass debug")
        logger.info("jarLibraryBean: {}", jarLibraryBean)
    }
}