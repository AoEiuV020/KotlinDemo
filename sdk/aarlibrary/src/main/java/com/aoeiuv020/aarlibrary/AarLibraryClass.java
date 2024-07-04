package com.aoeiuv020.aarlibrary;

import com.aoeiuv020.jarlibrary.JarLibraryBean;
import com.aoeiuv020.jarlibrary.JarLibraryClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * aar 模块里的主类，
 */
public class AarLibraryClass {
    public static final AarLibraryBean aarLibraryBean = new AarLibraryBean("AarLibrary", "AarLibraryBean");
    public static final JarLibraryBean jarLibraryBean = JarLibraryClass.Companion.getJarLibraryBean();

    private static class Lazy {
        public static Logger logger = LoggerFactory.getLogger("AarLibrary");
    }

    public static Logger getLogger() {
        return Lazy.logger;
    }

    public JarLibraryClass jarLibraryClass = new JarLibraryClass();

    public void init() {
        jarLibraryClass.init();
        getLogger().info("AarLibraryClass init");
        getLogger().debug("AarLibraryClass debug");
        getLogger().info("aarLibraryBean: {}", aarLibraryBean);
        getLogger().info("jarLibraryBean: {}", jarLibraryBean);
    }
}
