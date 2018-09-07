package cc.aoeiuv020.exception

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by AoEiuV020 on 2018.09.07-23:08:52.
 */
class ExceptionKtTest {
    @Test
    fun interruptTest() {
        try {
            interrupt("break,")
        } catch (e: IllegalStateException) {
            assertEquals("break,", e.message)
        }
    }
}