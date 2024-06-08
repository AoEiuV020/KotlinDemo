package com.aoeiuv020.androidlibrary;

import com.aoeiuv020.javalibrary.JavaLibraryBean;
import com.aoeiuv020.javalibrary.JavaLibraryClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AndroidLibraryClass {
    public static final AndroidLibraryBean androidLibraryBean = new AndroidLibraryBean("AndroidLibrary", "AndroidLibraryBean");
    public static final JavaLibraryBean javaLibraryBean = JavaLibraryClass.Companion.getJavaLibraryBean();

    private static class Lazy {
        public static Logger logger = LoggerFactory.getLogger("AndroidLibrary");
    }

    public static Logger getLogger() {
        return Lazy.logger;
    }

    public JavaLibraryClass javaLibraryClass = new JavaLibraryClass();

    public void init() {
        getLogger().info("AndroidLibraryClass init");
        getLogger().debug("AndroidLibraryClass debug");
        getLogger().info("androidLibraryBean: {}", androidLibraryBean);
        getLogger().info("javaLibraryBean: {}", javaLibraryBean);
        javaLibraryClass.init();
    }
}
