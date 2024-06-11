package com.aoeiuv020.kotlindemo

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class App : Application(){
    override fun onCreate() {
        super.onCreate()
        ctx = applicationContext
    }

    @SuppressLint("StaticFieldLeak")
    companion object {
        lateinit var ctx: Context
    }
}