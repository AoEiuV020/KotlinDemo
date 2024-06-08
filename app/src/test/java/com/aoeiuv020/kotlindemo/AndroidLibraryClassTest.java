package com.aoeiuv020.kotlindemo;

import com.aoeiuv020.androidlibrary.AndroidLibraryClass;

import org.junit.Test;
import org.slf4j.simple.SimpleLogger;

public class AndroidLibraryClassTest {
    static {
        System.setProperty(SimpleLogger.LOG_KEY_PREFIX + "JavaLibrary", "trace");
        System.setProperty(SimpleLogger.LOG_KEY_PREFIX + "AndroidLibrary", "trace");
    }

    @Test
    public void init() {
        var androidLibraryClass = new AndroidLibraryClass();
        androidLibraryClass.init();
        System.out.println(AndroidLibraryClass.androidLibraryBean);
        System.out.println(AndroidLibraryClass.javaLibraryBean);
    }
}