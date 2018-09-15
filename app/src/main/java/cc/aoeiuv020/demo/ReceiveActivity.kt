package cc.aoeiuv020.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_receive.*

class ReceiveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receive)

        val intent = requireNotNull(intent)
        tvIntent.text = StringBuilder()
                .appendln("intent: $intent")
                .appendln("action: ${intent.action}")
                .appendln("categories: ${intent.categories}")
                .apply {
                    intent.extras.keySet().forEach {
                        appendln("$it => ${intent.extras.get(it)}")
                    }
                }
                .toString()
    }
}
