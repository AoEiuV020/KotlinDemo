package cc.aoeiuv020.demo.adb.devconn

import com.cgutman.adblib.AdbCrypto

interface DeviceConnectionListener {

    val isConsole: Boolean
    fun notifyConnectionEstablished(devConn: DeviceConnection)

    fun notifyConnectionFailed(devConn: DeviceConnection, e: Exception)

    fun notifyStreamFailed(devConn: DeviceConnection, e: Exception)

    fun notifyStreamClosed(devConn: DeviceConnection)

    fun loadAdbCrypto(devConn: DeviceConnection): AdbCrypto

    fun receivedData(devConn: DeviceConnection, data: ByteArray, offset: Int, length: Int)
}
