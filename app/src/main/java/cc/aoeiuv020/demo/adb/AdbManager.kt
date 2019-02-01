package cc.aoeiuv020.demo.adb

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import cc.aoeiuv020.demo.adb.devconn.DeviceConnection
import cc.aoeiuv020.demo.adb.devconn.DeviceConnectionListener
import com.cgutman.adblib.AdbCrypto
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync

/**
 * Created by AoEiuV020 on 2019.02.01-14:23:43.
 */
object AdbManager : AnkoLogger {
    lateinit var crypto: AdbCrypto
    fun init(ctx: Context) {
        crypto = AdbUtils.readCryptoConfig(ctx.filesDir)
                ?: AdbUtils.writeNewCryptoConfig(ctx.filesDir)
    }

    fun loadAdbCrypto(): AdbCrypto {
        return crypto
    }

    private var connection: DeviceConnection? = null

    private val listeners: MutableList<ReceiveDataListener> = mutableListOf()

    fun addDataListener(listener: ReceiveDataListener) {
        listeners.add(listener)
    }

    fun removeDataListener(listener: ReceiveDataListener) {
        listeners.remove(listener)
    }

    fun register(lifecycleOwner: LifecycleOwner, listener: ReceiveDataListener) {
        lifecycleOwner.lifecycle.addObserver(
                object : LifecycleObserver {
                    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
                    fun onCreate() {
                        addDataListener(listener)
                    }

                    fun onDestory() {
                        removeDataListener(listener)
                    }
                }
        )
    }

    fun connect(host: String, port: Int) {
        if (connection?.isClosed == false) {
            return
        }
        connection = DeviceConnection(object : DeviceConnectionListener {
            override fun notifyConnectionEstablished(devConn: DeviceConnection) {
            }

            override fun notifyConnectionFailed(devConn: DeviceConnection, e: Exception) {
                connection = null
            }

            override fun notifyStreamFailed(devConn: DeviceConnection, e: Exception) {
                connection = null
            }

            override fun notifyStreamClosed(devConn: DeviceConnection) {
                connection = null
            }

            override fun loadAdbCrypto(devConn: DeviceConnection): AdbCrypto {
                return loadAdbCrypto()
            }

            override fun receivedData(devConn: DeviceConnection, data: ByteArray, offset: Int, length: Int) {
                listeners.forEach {
                    it.receive(data, offset, length)
                }
            }
        }, host, port).apply {
            startConnect()
        }
    }

    fun close() {
        // close时有网络操作，不能放主线程，
        connection?.let {
            doAsync {
                it.close()
            }
        }
    }

    fun send(command: String) {
        connection?.queueCommand(command)
    }
}