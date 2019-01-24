package cc.aoeiuv020.demo

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()

        assertEquals(BuildConfig.APPLICATION_ID, appContext.packageName)
    }

    @Test
    fun loggerTest() {
        val logger = LoggerFactory.getLogger("AndroidTest")
        logger.info(BuildConfig.APPLICATION_ID)
    }
}
