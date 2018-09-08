package cc.aoeiuv020.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cc.aoeiuv020.ssl.TLSSocketFactory
import cc.aoeiuv020.ssl.TrustManagerUtils
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.security.cert.X509Certificate

class MainActivity : AppCompatActivity() {
    private val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .cipherSuites(
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
                    *ConnectionSpec.MODERN_TLS.cipherSuites()!!.toTypedArray()
            )
            .build()

    private fun OkHttpClient.Builder.sslInclude(vararg certList: X509Certificate) = apply {
        TrustManagerUtils.include(certList.toSet()).let { tm ->
            sslSocketFactory((TLSSocketFactory(tm)), tm)
        }
    }

    private val client = OkHttpClient.Builder()
            .connectionSpecs(listOf(spec, ConnectionSpec.CLEARTEXT))
            .sslInclude()
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
