package cc.aoeiuv020.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_receive.*

class ReceiveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receive)

        val intent = requireNotNull(intent)
        val type = when {
            intent.type.startsWith("text") -> "文本"
            intent.type.startsWith("image") -> "图片"
            intent.type.startsWith("video") -> "视频"
        // 不可能未知，只过滤了这三种，
            else -> "未知"
        }
        tvIntent.text = StringBuilder()
                .appendln("intent: $intent")
                .appendln("type: $type")
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
