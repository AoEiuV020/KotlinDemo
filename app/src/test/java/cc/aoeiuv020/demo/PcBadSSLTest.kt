package cc.aoeiuv020.demo

import org.junit.Test

class PcBadSSLTest {
    private val b = BadSSLTest()
    @Test
    fun testHtml() {
        b.testHtml()
    }

    @Test
    fun testImage() {
        b.testImage()
    }
}