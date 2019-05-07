package cc.aoeiuv020

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cc.aoeiuv020.okhttp.OkHttpUtils
import cc.aoeiuv020.okhttp.get
import cc.aoeiuv020.okhttp.string
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity(), AnkoLogger {
    private val client = OkHttpUtils.client

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnGet.setOnClickListener {
            val url = etUrl.text.toString()
            doAsync({ e ->
                error { e }
                runOnUiThread {
                    toast(e.message.toString())
                }
            }) {
                val body = client.get(url)
                        .string()
                uiThread {
                    tvBody.text = body
                }
            }
        }
    }
}
