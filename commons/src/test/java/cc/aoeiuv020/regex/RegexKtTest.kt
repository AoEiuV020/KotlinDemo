package cc.aoeiuv020.regex

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by AoEiuV020 on 2018.09.07-23:25:24.
 */
class RegexKtTest {
    @Test
    fun regexTest() {
        assertEquals("AoEiuV020", "name: AoEiuV020".pick("name: (.*)").first())
    }
}