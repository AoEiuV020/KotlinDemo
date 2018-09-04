package cc.aoeiuv020.demo

import android.annotation.SuppressLint
import cc.aoeiuv020.demo.ssl.TLSSocketFactory
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import java.security.KeyStore
import java.security.cert.X509Certificate
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 * Created by AoEiuV020 on 2018.09.04-17:49:06.
 */

@Suppress("unused")
val trustAllManager: X509TrustManager = object : X509TrustManager {
    @SuppressLint("TrustAllX509TrustManager")
    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
    }

    @SuppressLint("TrustAllX509TrustManager")
    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return emptyArray()
    }
}

private val trustManager: X509TrustManager = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        .apply { init(null as KeyStore?) }
        .trustManagers
        .first {
            it is X509TrustManager
        } as X509TrustManager

// https://github.com/square/okhttp/issues/4053
// Add legacy cipher suite for Android 4
private val cipherSuites = ConnectionSpec.MODERN_TLS.cipherSuites()
        .orEmpty()
        .toMutableList().apply {
            if (!contains(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA)) {
                /*
                javax.net.ssl.SSLProtocolException: SSL handshake aborted: ssl=0xb88ec0b0: Failure in SSL library, usually a protocol error
            error:14077410:SSL routines:SSL23_GET_SERVER_HELLO:sslv3 alert handshake failure (external/openssl/ssl/s23_clnt.c:741 0x98947990:0x00000000)
                 */
                add(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA)
            }
        }

private val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
        .cipherSuites(*cipherSuites.toTypedArray())
        .build()

/**
 * 私有，外部不要用，不要改了这个builder, 需要时baseClient.newBuilder(),
 */
private val baseClientBuilder: OkHttpClient.Builder by lazy {
    OkHttpClient.Builder()
            // cleartext要明确指定，
            .connectionSpecs(listOf(spec, ConnectionSpec.CLEARTEXT))
            .sslSocketFactory((TLSSocketFactory(trustManager)), trustManager)
}

val baseClient: OkHttpClient = baseClientBuilder
        .build()
