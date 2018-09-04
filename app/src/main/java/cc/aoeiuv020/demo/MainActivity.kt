package cc.aoeiuv020.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient.Builder()
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnGet.setOnClickListener {
            val url = etUrl.text.toString()
            doAsync {
                val body = Request.Builder()
                        .url(url)
                        .build()
                        .let { client.newCall(it) }
                        .execute()
                        .body()
                        .let { requireNotNull(it) }
                        .string()
                uiThread {
                    tvBody.text = body
                }
            }
        }
    }
}
