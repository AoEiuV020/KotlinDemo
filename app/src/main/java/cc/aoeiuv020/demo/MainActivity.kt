package cc.aoeiuv020.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cc.aoeiuv020.demo.adb.AdbManager
import cc.aoeiuv020.demo.adb.devconn.DeviceConnection
import cc.aoeiuv020.demo.adb.devconn.DeviceConnectionAdapter
import cc.aoeiuv020.demo.adb.devconn.DeviceConnectionListener
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger

class MainActivity : AppCompatActivity(), AnkoLogger {
    private val listener: DeviceConnectionListener = object : DeviceConnectionAdapter() {
        override fun receivedString(devConn: DeviceConnection, data: String) {
            runOnUiThread {
                etConsole.text.append(data)
                etConsole.setSelection(etConsole.text.length)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AdbManager.init(this)

        btnConnect.setOnClickListener {
            AdbManager.connect(etHost.text.toString(), etPort.text.toString().toInt())
        }

        btnDisconnect.setOnClickListener {
            AdbManager.close()
        }

        btnSend.setOnClickListener {
            AdbManager.send(etCommand.text.toString())
        }

        AdbManager.register(this, listener)
    }

}
