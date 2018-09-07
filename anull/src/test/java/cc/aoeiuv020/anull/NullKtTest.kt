package cc.aoeiuv020.anull

import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

/**
 * Created by AoEiuV020 on 2018.09.07-22:58:42.
 */
class NullKtTest {
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
}