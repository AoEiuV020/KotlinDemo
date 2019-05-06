package cc.aoeiuv020

import org.junit.Test

class AndroidBadSSLTest {
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