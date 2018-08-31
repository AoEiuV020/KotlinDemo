package cc.aoeiuv020.demo

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        initReporter()
    }

    private fun initReporter() {
        Reporter.init(this)
    }
}
