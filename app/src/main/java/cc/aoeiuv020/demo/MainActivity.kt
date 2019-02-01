package cc.aoeiuv020.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cc.aoeiuv020.demo.adb.AdbManager
import cc.aoeiuv020.demo.adb.devconn.DeviceConnection
import cc.aoeiuv020.demo.adb.devconn.DeviceConnectionListener
import com.cgutman.adblib.AdbCrypto
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var connection: DeviceConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AdbManager.init(this)

        btnConnect.setOnClickListener {
            val host = etHost.text.toString()
            val port = etPort.text.toString().toInt()
            connection = DeviceConnection(object : DeviceConnectionListener {
                override val isConsole: Boolean = false
                override fun notifyConnectionEstablished(devConn: DeviceConnection) {
                    info { devConn }
                }

                override fun notifyConnectionFailed(devConn: DeviceConnection, e: Exception) {
                    info { devConn }
                    info { e }
                }

                override fun notifyStreamFailed(devConn: DeviceConnection, e: Exception) {
                    info { devConn }
                    info { e }
                }

                override fun notifyStreamClosed(devConn: DeviceConnection) {
                    info { devConn }
                }

                override fun loadAdbCrypto(devConn: DeviceConnection): AdbCrypto {
                    info { devConn }
                    return AdbManager.loadAdbCrypto()
                }

                override fun receivedData(devConn: DeviceConnection, data: ByteArray, offset: Int, length: Int) {
                    info { devConn }
                    info { data }
                    info { offset }
                    info { length }
                    etConsole.text.append(String(data, offset, length))
                }
            }, host, port)
            connection.startConnect()
        }

        btnDisconnect.setOnClickListener {
            connection.close()
        }

        btnSend.setOnClickListener {
            val command = etCommand.text.toString()
            connection.queueCommand(command)
        }
    }
}
