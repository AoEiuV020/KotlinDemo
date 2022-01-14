package cc.aoeiuv020

import org.junit.Assert.assertEquals
import org.junit.Test
import org.slf4j.LoggerFactory

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, (2 + 2).toLong())
    }

    @Test
    fun loggerTest() {
        val logger = LoggerFactory.getLogger("PCTest")
        logger.info(BuildConfig.APPLICATION_ID)
    }
}