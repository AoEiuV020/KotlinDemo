package cc.aoeiuv020.demo

import org.junit.Test

/**
 * Created by AoEiuV020 on 2018.09.04-15:19:30.
 */
class PcHttpsTest {
    private val t = HttpsTest()
    @Test
    fun intermediate() {
        t.intermediate()
    }

    @Test
    fun sslv3() {
        t.sslv3()
    }

    @Test
    fun clearText() {
        t.clearText()
    }

    @Test
    fun timeout() {
        t.timeout()
    }

    @Test
    fun trust() {
        t.trust()
    }

    @Test
    fun novel() {
        t.novel()
    }
}
