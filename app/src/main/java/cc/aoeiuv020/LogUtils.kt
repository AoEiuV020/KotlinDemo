package cc.aoeiuv020

import android.content.Intent
import android.util.Log

/**
 * Created by AoEiuV020 on 2019.07.11-22:10:32.
 */
object LogUtils {
    fun i(tag: String, intent: Intent) {
        val sb = StringBuilder()
        sb.append("intent: ")
        sb.appendln(intent.toString())
        sb.append("bundle: ")
        val extras = intent.extras ?: return
        for (key in extras.keySet()) {
            val value = extras[key]
            sb.appendln("$key -> $value")
        }
        Log.i(tag, sb.toString())
    }
}