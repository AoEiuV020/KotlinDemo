package cc.aoeiuv020.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvHello.setOnClickListener {
            info { "on click" }
            MobclickAgent.reportError(this, Exception("click tvHello,"))
        }

        tvHello.setOnLongClickListener {
            info { "on long click" }
            throw RuntimeException("long click tvHello,")
        }
    }
}
