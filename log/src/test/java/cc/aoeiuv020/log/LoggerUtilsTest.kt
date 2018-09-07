package cc.aoeiuv020.log

import cc.aoeiuv020.log.LoggerUtils.getLogger
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by AoEiuV020 on 2018.09.07-23:22:29.
 */
class LoggerUtilsTest {
    @Test
    fun getLoggerTest() {
        val logger = getLogger()
        assertEquals(this.javaClass.simpleName, logger.name)
    }
}