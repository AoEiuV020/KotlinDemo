package cc.aoeiuv020.string

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by AoEiuV020 on 2018.09.07-23:27:25.
 */
class StringKtTest {
    @Test
    fun stringTest() {
        assertEquals("a" to "b", "a|b".divide('|'))
    }
}