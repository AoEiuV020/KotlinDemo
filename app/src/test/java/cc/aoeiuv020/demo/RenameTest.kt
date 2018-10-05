package cc.aoeiuv020.demo

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * Created by AoEiuV020 on 2018.10.05-14:49:44.
 */
class RenameTest {
    @Test
    fun rename() {
        val file1 = File.createTempFile("pre", ".suf")
        val file2 = File.createTempFile("pre", ".suf")
        file1.renameTo(file2)
        try {
            assertFalse(file1.exists())
            assertTrue(file2.exists())
        } finally {
            file1.delete()
            file2.delete()
        }
    }
}