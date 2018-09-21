package cc.aoeiuv020.demo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = intent.getParcelableExtra<Intent>("share") ?: return
        val type = when {
            intent.type.startsWith("text") -> "文本"
            intent.type.startsWith("image") -> "图片"
            intent.type.startsWith("video") -> "视频"
        // 不可能未知，只过滤了这三种，
            else -> "未知"
        }
        textView.text = StringBuilder()
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

        val uri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
        contentResolver.openInputStream(uri).use { input ->
            openFileOutput("test", Context.MODE_PRIVATE).use { output ->
                input.copyTo(output)
            }
        }
    }
}
