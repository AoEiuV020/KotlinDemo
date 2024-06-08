package com.aoeiuv020.kotlindemo

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aoeiuv020.androidlibrary.AndroidLibraryClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AndroidLibraryClassTest {
    @Test
    fun init() {
        val androidLibraryClass = AndroidLibraryClass()
        androidLibraryClass.init()
        println(AndroidLibraryClass.androidLibraryBean)
        println(AndroidLibraryClass.javaLibraryBean)
    }
}