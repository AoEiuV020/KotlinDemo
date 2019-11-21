package cc.aoeiuv020

import com.leon.channel.writer.IdValueWriter
import org.junit.Test
import java.io.File

class DataWriteTest {
    @Test
    fun write() {
        val apk = File(".\\build\\outputs\\apk\\debug\\KotlinDemo-1.0-debug.apk")
        val value = """
            {
            "testKey":"testValue"
            }
        """.trimIndent()
        IdValueWriter.addIdValue(apk, 0x12345678, value.toByteArray(), false)
    }
}
