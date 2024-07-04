package com.aoeiuv020.aarlibrary;

import org.junit.Test;
import org.slf4j.simple.SimpleLogger;

public class AarLibraryClassTest {
    static {
        System.setProperty(SimpleLogger.LOG_KEY_PREFIX + "JarLibrary", "trace");
        System.setProperty(SimpleLogger.LOG_KEY_PREFIX + "AarLibrary", "trace");
    }

    @Test
    public void init() {
        var aarLibraryClass = new AarLibraryClass();
        aarLibraryClass.init();
        System.out.println(AarLibraryClass.aarLibraryBean);
        System.out.println(AarLibraryClass.jarLibraryBean);
    }
}