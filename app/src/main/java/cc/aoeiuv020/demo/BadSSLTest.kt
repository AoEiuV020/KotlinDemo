package cc.aoeiuv020.demo

import cc.aoeiuv020.gson.toBean
import cc.aoeiuv020.regex.compilePattern
import cc.aoeiuv020.regex.pick
import cc.aoeiuv020.ssl.TLSSocketFactory
import cc.aoeiuv020.ssl.TrustManagerUtils
import okhttp3.*
import java.io.IOException
import java.security.cert.X509Certificate

/**
 * https://badssl.com/
 */
class BadSSLTest {
    private val setJson = """
[
  {
    heading: "Not Secure",
    success: "no",
    fail: "yes",
    subdomains: [
      {subdomain: "expired"},
      {subdomain: "wrong.host"},
      {subdomain: "self-signed"},
      {subdomain: "untrusted-root"},

      {subdomain: "sha1-intermediate"},
      {subdomain: "rc4"},
      {subdomain: "rc4-md5"},
      {subdomain: "dh480"},
      {subdomain: "dh512"},
      {subdomain: "dh1024"},
      {subdomain: "superfish"},
      {subdomain: "edellroot"},
      {subdomain: "dsdtestprovider"},
      {subdomain: "preact-cli"},
      {subdomain: "webpack-dev-server"},
      {subdomain: "null"}
    ]
  },
  {
    heading: "Bad Certificates",
    success: "maybe",
    fail: "yes",
    subdomains: [
      {subdomain: "revoked"},
      {subdomain: "pinning-test"},
      {subdomain: "invalid-expected-sct"}
    ]
  },
  {
    heading: "Legacy",
    success: "maybe",
    fail: "yes",
      subdomains: [
      {subdomain: "tls-v1-0", port: 1010},
      {subdomain: "tls-v1-1", port: 1011},
      {subdomain: "cbc"},
      {subdomain: "3des"}
    ]
  },
  {
    heading: "Secure",
    success: "yes",
    fail: "no",
    subdomains: [
      {subdomain: "tls-v1-2", port: 1012},
      {subdomain: "sha256"},
      {subdomain: "sha384"},
      {subdomain: "sha512"},
      {subdomain: "rsa2048"},
      {subdomain: "ecc256"},
      {subdomain: "ecc384"},
      {subdomain: "extended-validation"},
      {subdomain: "mozilla-modern"}
    ]
  },
  {
    heading: "Secure but Weird",
    success: "yes",
    fail: "maybe",
      subdomains: [
      {subdomain: "1000-sans"},
      {subdomain: "10000-sans"},
      {subdomain: "rsa8192"},
      {subdomain: "no-subject"},
      {subdomain: "no-common-name"},
      {subdomain: "incomplete-chain"}
    ]
  }
]
    """.trimIndent()
    private val sites = listOf(
            "https://expired.badssl.com/",
            "https://wrong.host.badssl.com/",
            "https://self-signed.badssl.com/",
            "https://untrusted-root.badssl.com/",
            "https://revoked.badssl.com/",
            "https://pinning-test.badssl.com/",
            "https://no-common-name.badssl.com/",
            "https://no-subject.badssl.com/",
            "https://incomplete-chain.badssl.com/",
            "https://sha1-intermediate.badssl.com/",
            "https://sha256.badssl.com/",
            "https://sha384.badssl.com/",
            "https://sha512.badssl.com/",
            "https://1000-sans.badssl.com/",
            "https://10000-sans.badssl.com/",
            "https://ecc256.badssl.com/",
            "https://ecc384.badssl.com/",
            "https://rsa2048.badssl.com/",
            "https://rsa4096.badssl.com/",
            "https://rsa8192.badssl.com/",
            "https://extended-validation.badssl.com/",
            "https://badssl.com/download/",
            "https://client.badssl.com/",
            "https://client-cert-missing.badssl.com/",
            "https://mixed-script.badssl.com/",
            "https://very.badssl.com/",
            "https://mixed.badssl.com/",
            "https://mixed-favicon.badssl.com/",
            "https://mixed-form.badssl.com/",
            "http://http.badssl.com/",
            "http://http-textarea.badssl.com/",
            "http://http-password.badssl.com/",
            "http://http-login.badssl.com/",
            "http://http-dynamic-login.badssl.com/",
            "http://http-credit-card.badssl.com/",
            "https://cbc.badssl.com/",
            "https://rc4-md5.badssl.com/",
            "https://rc4.badssl.com/",
            "https://3des.badssl.com/",
            "https://null.badssl.com/",
            "https://mozilla-old.badssl.com/",
            "https://mozilla-intermediate.badssl.com/",
            "https://mozilla-modern.badssl.com/",
            "https://dh480.badssl.com/",
            "https://dh512.badssl.com/",
            "https://dh1024.badssl.com/",
            "https://dh2048.badssl.com/",
            "https://dh-small-subgroup.badssl.com/",
            "https://dh-composite.badssl.com/",
            "https://static-rsa.badssl.com/",
            "https://tls-v1-0.badssl.com:1010/",
            "https://tls-v1-1.badssl.com:1011/",
            "https://tls-v1-2.badssl.com:1012/",
            "https://invalid-expected-sct.badssl.com/",
            "https://hsts.badssl.com/",
            "https://upgrade.badssl.com/",
            "https://preloaded-hsts.badssl.com/",
            "https://subdomain.preloaded-hsts.badssl.com/",
            "https://https-everywhere.badssl.com/",
            "https://spoofed-favicon.badssl.com/",
            "https://long-extended-subdomain-name-containing-many-letters-and-dashes.badssl.com/",
            "https://longextendedsubdomainnamewithoutdashesinordertotestwordwrapping.badssl.com/",
            "https://superfish.badssl.com/",
            "https://edellroot.badssl.com/",
            "https://dsdtestprovider.badssl.com/",
            "https://preact-cli.badssl.com/",
            "https://webpack-dev-server.badssl.com/",
            "https://captive-portal.badssl.com/",
            "https://mitm-software.badssl.com/",
            "https://sha1-2016.badssl.com/",
            "https://sha1-2017.badssl.com/",
            "https://testsafebrowsing.appspot.com/",
            "https://www.ssllabs.com/ssltest/viewMyClient.html",
            "https://mitm.watch/"
    )

    private val testNamePattern = compilePattern("\\/\\/(.*)\\.badssl.com")

    fun testHtml() = sites.map {
        try {
            it.pick(testNamePattern).first()
        } catch (_: Exception) {
            it
        } to get(it)
    }.forEach { (name, result) ->
        println("$name => $result")
    }

    fun testImage() {
        val siteSet = setJson.toBean<List<SiteSet>>()
        siteSet.forEach { sites ->
            println(sites.heading)
            sites.subdomains.forEach { config ->
                println(config.subdomain)
                var origin = "https://${config.subdomain}.badssl.com"
                if (config.port != null) {
                    origin += ":" + config.port
                }
                val url = "$origin/test/dashboard/small-image.png"
                val success = get(url)
                val result = if (success) sites.success else sites.fail
                println((if (success) "O" else "X") + ", " + result)
            }
        }
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

    private val client = OkHttpClient.Builder()
            .connectionSpecs(listOf(spec, ConnectionSpec.CLEARTEXT))
            .sslInclude()
            .build()

    private fun get(url: String): Boolean {
        return okhttpGet(url)
    }

    private fun okhttpGet(url: String, client: Call.Factory = this.client): Boolean = try {
        println(url)
        val status = Request.Builder()
                .url(url)
                .build()
                .let { client.newCall(it) }
                .execute()
                .code()
        println("status = $status")
        true
    } catch (_: IOException) {
        false
    }

    data class SiteSet(
            val fail: String,
            val heading: String,
            val subdomains: List<Subdomain>,
            val success: String
    )

    data class Subdomain(
            val subdomain: String,
            val port: Int?
    )
}