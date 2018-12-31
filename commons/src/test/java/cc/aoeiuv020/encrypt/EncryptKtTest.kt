package cc.aoeiuv020.encrypt

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by AoEiuV020 on 2018.09.07-23:20:08.
 */
class EncryptKtTest {
    @Test
    fun base64Test() {
        assertEquals("QW9FaXVWMDIw", "AoEiuV020".toByteArray().base64())
    }
}