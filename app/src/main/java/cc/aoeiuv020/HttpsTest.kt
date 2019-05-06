package cc.aoeiuv020

import cc.aoeiuv020.ssl.TLSSocketFactory
import cc.aoeiuv020.ssl.TrustManagerUtils
import okhttp3.*
import java.security.cert.X509Certificate

/**
 * Created by AoEiuV020 on 2018.09.04-15:19:30.
 */
class HttpsTest {
    init {
/*
        System.setProperty("org.slf4j.simpleLogger.log.TrustManagerUtils", "trace")
*/
        System.setProperty("org.slf4j.simpleLogger.log.CertificationUtils", "debug")
    }

    // 这些网站证书链有问题，只有自己的证书，导致一般验证都不通过，
    // 但是chrome和firefox之类照样能拿到颁发者，然后信任，
    fun intermediate() {
        get("https://www.qingkan9.com")
        get("https://www.7dsw.com")
        get("https://www.snwx8.com")
    }

    fun sslv3() {
        get("https://lnovel.cc")
//        get("https://www.shangshu.cc")
    }

    fun clearText() {
        get("https://www.haxwx11.com")
    }

    fun timeout() {
        // 这个可能是墙内上不去了，
//        get("https://www.gxwztv.com")
    }

    fun trust() {
        get("https://www.x23us.com")
        get("https://www.zhuishu.tw")
    }

    fun novel() {
        get("https://www.lread.net")
        get("https://www.biqubao.com")
        get("https://www.miaobige.com")
        get("https://www.qidian.com")
        get("https://www.exiaoshuo.cc")
        get("https://www.yssm.tv")
        get("https://www.liewen.cc")
        get("https://www.wenxuemi.com")
        get("https://www.piaotian.com")
        get("https://www.ymoxuan.com")
        get("https://www.bqg5200.com")
        get("https://www.kuxiaoshuo.com")
        get("https://www.x23us.com")
        get("https://www.zwdu.com")
    }

    private fun get(url: String) {
        okhttpGet(url)
    }

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

    private fun okhttpGet(url: String, client: Call.Factory = OkHttpClient.Builder()
            .connectionSpecs(listOf(spec, ConnectionSpec.CLEARTEXT))
            .sslInclude()
            .build()) {
        println(url)
        val body = Request.Builder()
                .url(url)
                .build()
                .let { client.newCall(it) }
                .execute()
                .body()
                .let { requireNotNull(it) }
                .string()
        println(body.length)
    }
}