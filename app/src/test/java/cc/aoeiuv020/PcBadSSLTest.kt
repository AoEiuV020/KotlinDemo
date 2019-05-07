package cc.aoeiuv020

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

    @Test
    fun expired() {
        b.singleTest("https://expired.badssl.com/test/dashboard/small-image.png")
    }
}