package cc.aoeiuv020.demo

import cc.aoeiuv020.anull.notNull
import cc.aoeiuv020.atry.tryOrNul
import cc.aoeiuv020.encrypt.base64
import cc.aoeiuv020.exception.interrupt
import cc.aoeiuv020.log.LoggerUtils.getLogger
import cc.aoeiuv020.regex.pick
import cc.aoeiuv020.string.divide
import cc.aoeiuv020.type.type
import org.junit.Assert.*
import org.junit.Test

/**
 * 跑一遍其他所有模块的测试，证明确实依赖了，
 *
 * Created by AoEiuV020 on 2018.09.07-23:18:25.
 */
class PcCommonsKtTest {
    @Test
    fun nullTest() {
        val s: String? = null
        try {
            s.notNull()
            fail()
        } catch (e: IllegalArgumentException) {
            assertEquals("Required class java.lang.String was null.", e.message)
        }
    }

    @Test
    fun tryTest() {
        assertNull(tryOrNul {
            fail()
        })
    }

    @Test
    fun base64Test() {
        assertEquals("QW9FaXVWMDIw", "AoEiuV020".toByteArray().base64())
    }

    @Test
    fun interruptTest() {
        try {
            interrupt("break,")
        } catch (e: IllegalStateException) {
            assertEquals("break,", e.message)
        }
    }

    @Test
    fun getLoggerTest() {
        val logger = getLogger()
        assertEquals(this.javaClass.simpleName, logger.name)
    }

    @Test
    fun regexTest() {
        assertEquals("AoEiuV020", "name: AoEiuV020".pick("name: (.*)").first())
    }

    @Test
    fun stringTest() {
        assertEquals("a" to "b", "a|b".divide('|'))
    }

    @Test
    fun typeTest() {
        assertEquals("class java.lang.String", type<String>().toString())
        assertEquals("java.util.List<? extends java.lang.String>", type<List<String>>().toString())
        assertEquals("java.util.Map<java.lang.Integer, ? extends java.util.List<? extends java.lang.String>>", type<Map<Int, List<String>>>().toString())
    }
}