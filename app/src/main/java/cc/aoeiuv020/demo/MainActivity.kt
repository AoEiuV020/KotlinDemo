package cc.aoeiuv020.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cc.aoeiuv020.demo.adb.AdbManager
import cc.aoeiuv020.demo.adb.ReceiveDataListener
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger

class MainActivity : AppCompatActivity(), AnkoLogger, ReceiveDataListener {

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

        AdbManager.register(this, this)
    }

    override fun receive(data: ByteArray, offset: Int, length: Int) {
        runOnUiThread {
            etConsole.text.append(String(data, offset, length))
            etConsole.setSelection(etConsole.text.length)
        }
    }

}
