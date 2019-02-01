package cc.aoeiuv020.demo.adb

/**
 * Created by AoEiuV020 on 2019.02.01-16:13:41.
 */
interface ReceiveDataListener {
    fun receive(data: ByteArray, offset: Int, length: Int)
}