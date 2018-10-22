package cc.aoeiuv020.demo.bind

import android.os.Bundle
import android.os.Process
import android.support.v7.app.AppCompatActivity
import cc.aoeiuv020.demo.R
import kotlinx.android.synthetic.main.activity_bind_first.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity

open class BindActivityFirst : AppCompatActivity(), AnkoLogger {
    companion object {
        const val KEY_INTENT_ID = "intentId"
        var sId: Int = 0
    }

    // activity崩溃时保留栈记录，finish时打开前一个，保留intent参数，
    private val id: Int = sId++
    private val intentId: String?
        get() = intent?.getStringExtra(KEY_INTENT_ID)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bind_first)
        info { "onCreate: $id, $intentId" }

        title = "First"

        btnStart.setOnClickListener {
            NormalService.start(this, "$title start")
        }
        btnBind.setOnClickListener {
            NormalService.bind(this, "$title bind")
        }
        btnUnBind.setOnClickListener {
            NormalService.unbind(this, "$title unbind")
        }
        btnStop.setOnClickListener {
            NormalService.stop(this, "$title stop")
        }
        btnFinish.setOnClickListener {
            finish()
        }
        btnKillSelf.setOnClickListener {
            android.os.Process.killProcess(Process.myPid())
            System.exit(0)
        }
        btnFirst.setOnClickListener {
            startActivity<BindActivityFirst>(KEY_INTENT_ID to id.toString())
        }
        btnSecond.setOnClickListener {
            startActivity<BindActivitySecond>(KEY_INTENT_ID to id.toString())
        }
    }

    override fun onDestroy() {
        info { "onDestroy: $id, $intentId" }
        // 绑定的服务不解绑不行，
        NormalService.unbind(this, "$title destroy")
        super.onDestroy()
    }
}
