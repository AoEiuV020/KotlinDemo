package com.aoeiuv020.jarlibrary

import org.slf4j.LoggerFactory

/**
 * jar library class,
 */
class JarLibraryClass {
    companion object {
        val jarLibraryBean = JarLibraryBean("JarLibrary", "JarLibraryBean")

        /**
         * jar library内部使用的logger,
         */
        internal val logger by lazy { LoggerFactory.getLogger("JarLibrary") }
    }

    /**
     * 初始化，
     */
    fun init() {
        logger.info("JarLibraryClass init")
        logger.debug("JarLibraryClass debug")
        logger.info("jarLibraryBean: {}", jarLibraryBean)
    }
}