package cc.aoeiuv020

import android.content.Context
import androidx.multidex.MultiDexApplication
import java.util.*

class App : MultiDexApplication() {
    companion object {
        @Suppress("DEPRECATION")
        fun initLanguage(ctx: Context) {
            val locale = Locale("en")
            Locale.setDefault(locale)
            val r = ctx.resources
            val c = r.configuration
            c.locale = locale
            r.updateConfiguration(c, r.displayMetrics)
        }
    }

    override fun onCreate() {
        super.onCreate()
        initLanguage(this)
    }
}
