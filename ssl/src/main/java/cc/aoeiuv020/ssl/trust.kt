package cc.aoeiuv020.ssl

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.security.KeyStore
import java.security.Principal
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 * Created by AoEiuV020 on 2018.09.07-04:34:31.
 */
@Suppress("unused")
object TrustManagerUtils {
    /**
     * trace级别打印所有经手的证书，
     */
    private val logger: Logger = LoggerFactory.getLogger("TrustManagerUtils")

    /**
     * 信任所有证书，
     */
    fun allowAll(): X509TrustManager = TrustManagerAllowAll()

    /**
     * 只信任certList中包含的证书，
     */
    fun only(certList: Set<X509Certificate>): X509TrustManager = TrustManagerOnly(certList)

    /**
     * 信任系统证书加上certList中的证书，
     */
    fun include(certList: Set<X509Certificate>): X509TrustManager = TrustManagerInclude(certList)

    /**
     * 信任所有证书，
     */
    private class TrustManagerAllowAll : X509TrustManager {
        @SuppressWarnings("TrustAllX509TrustManager")
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            if (logger.isTraceEnabled) {
                chain?.forEach {
                    logger.trace(CertificationUtils.certificateToBase64(it))
                }
            }
        }

        @SuppressWarnings("TrustAllX509TrustManager")
        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            if (logger.isTraceEnabled) {
                chain?.forEach {
                    logger.trace(CertificationUtils.certificateToBase64(it))
                }
            }
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return emptyArray()
        }
    }

    /**
     * 只信任certList中包含的证书，
     */
    private class TrustManagerOnly(
            private val certList: Set<X509Certificate>
    ) : X509TrustManager {
        @SuppressWarnings("TrustAllX509TrustManager")
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            if (logger.isTraceEnabled) {
                chain?.forEach {
                    logger.trace(CertificationUtils.certificateToBase64(it))
                }
            }
            // 不做客户端验证，
        }

        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            if (logger.isTraceEnabled) {
                chain?.forEach {
                    logger.trace(CertificationUtils.certificateToBase64(it))
                }
            }
            // 证书链中只要有一个受信任就可以，也就是颁发者受信任就可以，
            chain?.firstOrNull {
                certList.contains(it)
            } ?: throw CertificateException("该证书不受信任，")
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            // 不信任任何签发者证书，因为主动信任那些证书，不代表信任那些证书签发的证书，
            return emptyArray()
        }
    }


    /**
     * 信任系统证书加上certList中的证书，
     */
    private class TrustManagerInclude(
            certList: Set<X509Certificate>
    ) : X509TrustManager {
        /**
         * 系统默认的证书管理器，
         */
        private val tmDefault: X509TrustManager = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                .apply { init(null as KeyStore?) }
                .trustManagers
                .first {
                    it is X509TrustManager
                } as X509TrustManager

        /**
         * 所有信任的证书，
         * 遇到信任的就加上，因为可能是签发者受信任，
         */
        private val certList: MutableSet<Principal> = certList.map { it.subjectDN }
                .toMutableSet()
                .apply {
                    addAll(tmDefault.acceptedIssuers.map { it.subjectDN })
                }

        private val acceptedIssuerSet: Set<X509Certificate> = tmDefault.acceptedIssuers.toSet()

        @SuppressWarnings("TrustAllX509TrustManager")
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            if (logger.isTraceEnabled) {
                chain?.forEach {
                    logger.trace(CertificationUtils.certificateToBase64(it))
                }
            }
            // 不做客户端验证，
        }

        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            if (logger.isTraceEnabled) {
                chain?.forEach {
                    logger.trace(CertificationUtils.certificateToBase64(it))
                }
            }
            try {
                tmDefault.checkServerTrusted(chain, authType)
            } catch (e: CertificateException) {
                // 不受系统信任的证书才判断是否在主动信任列表中，
                // 证书链中只要有一个受信任就可以，也就是颁发者受信任就可以，
                chain?.firstOrNull {
                    trustChain(it)
                } ?: throw CertificateException("该证书不受信任："
                        + chain?.firstOrNull()?.let { CertificationUtils.certificateToBase64(it) })
            }
        }

        /**
         * 递归补齐证书链再判断该证书是否受信任，
         */
        private fun trustChain(cert: X509Certificate): Boolean {
            // 可以是证书本身是个受信任的签发者证书，也可以是证书的签发者受信任，
            // 这样处理是因为有的网站证书链只部署了自己的证书，没有颁发者的证书，只能从AuthorityInformationAccess判断，
            // 比如COMODO RSA Domain Validation Secure Server CA签发的数个网站，比如https://www.snwx8.com/，
            return exists(cert) || try {
                // 最终实现equals方法的类是X500Name，应该有好好的判断签名，没有的话也没办法了，
                val issuer = CertificationUtils.getIssuer(cert)
                if (issuer == null) {
                    false
                } else {
                    cert.verify(issuer.publicKey)
                    trustChain(issuer)
                }
            } catch (e: Exception) {
                // 颁发者是假的，
                e.printStackTrace()
                false
            }.also { trust ->
                if (trust) {
                    // 通过信任后证书链所有证书加入信任列表，
                    certList.add(cert.subjectDN)
                }
            }
        }

        private fun exists(cert: X509Certificate): Boolean {
            return certList.contains(cert.subjectDN) || certList.contains(cert.issuerDN)
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            // 只信任系统的签发者证书，因为主动信任那些证书，不代表信任那些证书签发的证书，
            return tmDefault.acceptedIssuers
        }
    }
}
