package cc.aoeiuv020.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cc.aoeiuv020.okhttp.OkhttpUtils
import cc.aoeiuv020.okhttp.get
import cc.aoeiuv020.okhttp.string
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity() {
    private val client = OkhttpUtils.client

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnGet.setOnClickListener {
            val url = etUrl.text.toString()
            doAsync({ e ->
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
