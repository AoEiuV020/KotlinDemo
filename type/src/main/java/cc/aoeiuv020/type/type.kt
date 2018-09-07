@file:Suppress("unused")

package cc.aoeiuv020.type

import java.lang.reflect.Type

/**
 * Created by AoEiuV020 on 2018.09.07-22:42:20.
 */
inline fun <reified T> type(): Type = object : TypeToken<T>() {}.type
