@file:Suppress("unused")

package cc.aoeiuv020.log

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * 定义一系列拓展，
 * 比如slf4j的，主要就是先判断再执行lambda,
 * Created by AoEiuV020 on 2017.10.02-16:01:09.
 */
object LoggerUtils {
    inline fun <reified T> getLogger(): Logger = getLogger(T::class.java)
    inline fun <reified T> T.getLogger(): Logger = getLogger(T::class.java)
    fun getLogger(clazz: Class<*>): Logger {
        return LoggerFactory.getLogger(clazz.simpleName)
    }
}

inline fun Logger.trace(message: () -> Any?) {
    if (isTraceEnabled) {
        trace("{}", message().toString())
    }
}

inline fun Logger.debug(e: Throwable, message: () -> Any?) {
    if (isDebugEnabled) {
        debug(message().toString(), e)
    }
}

inline fun Logger.debug(message: () -> Any?) {
    if (isDebugEnabled) {
        debug("{}", message().toString())
    }
}

inline fun Logger.info(message: () -> Any?) {
    if (isInfoEnabled) {
        info("{}", message().toString())
    }
}

inline fun Logger.warn(message: () -> Any?) {
    if (isWarnEnabled) {
        warn("{}", message().toString())
    }
}

inline fun Logger.error(message: () -> Any?) {
    if (isErrorEnabled) {
        error("{}", message().toString())
    }
}

inline fun Logger.error(e: Throwable, message: () -> Any?) {
    if (isErrorEnabled) {
        error(message().toString(), e)
    }
}
