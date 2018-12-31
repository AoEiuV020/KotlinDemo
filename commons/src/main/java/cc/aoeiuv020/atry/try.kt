@file:Suppress("unused")

package cc.aoeiuv020.atry

/**
 * Created by AoEiuV020 on 2018.09.07-23:02:32.
 */

inline fun <T> tryOrNul(block: () -> T?): T? = try {
    block()
} catch (_: Throwable) {
    null
}

