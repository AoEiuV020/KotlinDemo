package cc.aoeiuv020.demo

import android.os.Build
import okhttp3.Request
import org.junit.Assert.fail
import org.junit.Test
import javax.net.ssl.SSLHandshakeException

/**
 * Created by AoEiuV020 on 2018.09.04-15:19:30.
 */
class HttpsTest {
    @Test
    fun sslv3() {
        get("https://lnovel.cc")
        get("https://www.shangshu.cc")
    }

    @Test
    fun clearText() {
        // 这网站可能超时，
        get("https://www.haxds.com")
    }

    @Test
    fun timeout() {
        // 这个可能是墙内上不去了，
//        get("https://www.gxwztv.com")
    }

    @Test
    fun trust() {
        // 这个厂商不受android4.4信任，内置浏览器也是报错的，
        /*
        issuer: C=CN,O=TrustAsia Technologies\, Inc.,OU=Domain Validated SSL,CN=TrustAsia TLS RSA CA G8
         */
        try {
            get("https://www.zhuishu.tw")
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                fail()
            }
        } catch (e: SSLHandshakeException) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fail()
            }
        }
    }

    @Test
    fun novel() {
        get("https://www.lread.net")
        get("https://www.biqubao.com")
        get("https://www.miaobige.com")
        get("https://www.qidian.com")
        get("https://www.exiaoshuo.cc")
        get("https://www.snwx8.com")
        get("https://www.yssm.org")
        get("https://www.liewen.cc")
        get("https://www.wenxuemi.com")
        get("https://www.qingkan9.com")
        get("https://www.7dsw.com")
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

    private fun okhttpGet(url: String) {

        val body = Request.Builder()
                .url(url)
                .build()
                .let { baseClient.newCall(it) }
                .execute()
                .body()
                .let { requireNotNull(it) }
                .string()
        println(body.take(200))
    }
}