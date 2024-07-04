package com.aoeiuv020.androidlibrary;

import com.aoeiuv020.aarlibrary.AarLibraryBean;
import com.aoeiuv020.aarlibrary.AarLibraryClass;
import com.aoeiuv020.javalibrary.JavaLibraryBean;
import com.aoeiuv020.javalibrary.JavaLibraryClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AndroidLibraryClass {
    public static final AndroidLibraryBean androidLibraryBean = new AndroidLibraryBean("AndroidLibrary", "AndroidLibraryBean");
    public static final JavaLibraryBean javaLibraryBean = JavaLibraryClass.Companion.getJavaLibraryBean();
    public static final AarLibraryBean aarLibraryBean = AarLibraryClass.aarLibraryBean;

    private static class Lazy {
        public static Logger logger = LoggerFactory.getLogger("AndroidLibrary");
    }

    public static Logger getLogger() {
        return Lazy.logger;
    }

    public JavaLibraryClass javaLibraryClass = new JavaLibraryClass();
    public AarLibraryClass aarLibraryClass = new AarLibraryClass();

    public void init() {
        javaLibraryClass.init();
        aarLibraryClass.init();
        getLogger().info("AndroidLibraryClass init");
        getLogger().debug("AndroidLibraryClass debug");
        getLogger().info("androidLibraryBean: {}", androidLibraryBean);
        getLogger().info("javaLibraryBean: {}", javaLibraryBean);
        getLogger().info("aarLibraryBean: {}", aarLibraryBean);
    }
}
