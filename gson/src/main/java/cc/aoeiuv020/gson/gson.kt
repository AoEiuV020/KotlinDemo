@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package cc.aoeiuv020.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.util.*

/**
 * Created by AoEiuV020 on 2018.09.24-14:26:47.
 */
object GsonUtils {
    val gsonBuilder: GsonBuilder
        get() = GsonBuilder()
                .registerTypeAdapter(Date::class.java, DateTypeAdapter)
    val gson: Gson = gsonBuilder
            .create()
}