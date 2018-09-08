package cc.aoeiuv020.ssl

import cc.aoeiuv020.encrypt.base64
import cc.aoeiuv020.log.LoggerUtils.getLogger
import org.bouncycastle.asn1.DERIA5String
import org.bouncycastle.asn1.x509.AccessDescription
import org.bouncycastle.asn1.x509.AuthorityInformationAccess
import org.bouncycastle.asn1.x509.X509Extensions
import org.bouncycastle.x509.extension.X509ExtensionUtil
import org.slf4j.Logger
import java.net.URL
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.*

/**
 * 证书操作的工具类，
 *
 * Created by AoEiuV020 on 2018.09.07-05:08:28.
 */
@Suppress("MemberVisibilityCanBePrivate")
object CertificationUtils {
    private val logger: Logger = getLogger()
    private val cacheCert = WeakHashMap<String, X509Certificate>()
    /**
     * 下载该证书的签发者证书，
     */
    fun getIssuer(cert: X509Certificate): X509Certificate? {
        logger.debug("getIssuer: subject: {}", cert.subjectDN.name)
        logger.debug("getIssuer: issuer: {}", cert.issuerDN.name)
        try {
            val url = getIssuerUrl(cert) ?: return null
            logger.debug("getIssuer url: {}", url)
            return cacheCert.getOrPut(url) {
                base64ToCertificate(
                        certificateBytesToBase64(
                                // 直接用原生URL下载中间证书，使用系统信任，中间证书所在的网站不可能不受信任，
                                URL(url).openStream().readBytes()
                        )
                )
            }.also {
                logger.debug("getIssuer: cached issuer: {}", cert.issuerDN.name)
            }
        } catch (e: Exception) {
            throw CertificateException("获取签发者失败，", e)
        }
    }

    /**
     * 从AuthorityInformationAccess获得签发者，
     * 自签发证书返回null,
     */
    private fun getIssuerUrl(cert: X509Certificate): String? {
        if (cert.issuerDN == cert.subjectDN) {
            // 自签发证书直接返回空，避免死循环获取签发者，
            return null
        }
        // TODO: 移除bouncycastle，自己实现这个提取，
        // bouncycastle太重，只用到AuthorityInformationAccess有点浪费，
        val aia = AuthorityInformationAccess.getInstance(X509ExtensionUtil.fromExtensionValue(cert.getExtensionValue(X509Extensions.AuthorityInfoAccess.id)))
        return aia.accessDescriptions.firstOrNull {
            it.accessMethod == AccessDescription.id_ad_caIssuers
        }?.accessLocation?.name?.let { DERIA5String.getInstance(it).string }
    }

    private const val BEGIN_CERTIFICATE = "-----BEGIN CERTIFICATE-----"
    private const val END_CERTIFICATE = "-----END CERTIFICATE-----"

    fun certificateBytesToBase64(bytes: ByteArray): String {
        val sb = StringBuilder()
        sb.appendln(BEGIN_CERTIFICATE)
        val encoded = bytes.base64()
        var start = 0
        while (start < encoded.length) {
            // 固定一行64个字符，与chrome导出的证书一致，
            val end = (start + 64)
                    // 不能超过字符串长度，
                    .takeUnless { it > encoded.length }
                    ?: encoded.length
            sb.appendln(encoded.substring(start until end))
            start = end
        }
        sb.appendln(END_CERTIFICATE)
        return sb.toString()
    }

    fun base64ToCertificate(base64: String): X509Certificate {
        return CertificateFactory.getInstance("X.509")
                .generateCertificate(base64.byteInputStream())
                as X509Certificate
    }

    fun certificateToBase64(cert: X509Certificate): String {
        return certificateBytesToBase64(cert.encoded)
    }
}