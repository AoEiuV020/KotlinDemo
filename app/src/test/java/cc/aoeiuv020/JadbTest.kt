package cc.aoeiuv020

import org.junit.Test
import se.vidstige.jadb.JadbConnection

/**
 * Created by AoEiuV020 on 2019.02.02-20:26:32.
 */
class JadbTest {
    @Test
    fun jadb() {
        val conn = JadbConnection()
        val devices = conn.devices
        devices.forEach {
            println("${it.serial}, ${it.state}")
        }
    }
}