package cc.aoeiuv020.demo

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/**
 * Created by AoEiuV020 on 2018.10.05-13:29:27.
 */
@RunWith(AndroidJUnit4::class)
class FileTest {
    @Test
    fun tmpFile() {
        val appContext = InstrumentationRegistry.getTargetContext()

        val tFile = File.createTempFile("pre", ".suf")
        // createTempFile默认是在java.io.tmpdir下，等于cache目录下，
        assertEquals(tFile, appContext.cacheDir.resolve(tFile.name))
        assertEquals(appContext.cacheDir.absolutePath, System.getProperty("java.io.tmpdir", "."))
    }
}