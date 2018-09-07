@file:Suppress("unused")

package cc.aoeiuv020.encrypt

import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by AoEiuV020 on 2018.04.18-10:19:39.
 */

/**
 * java支持的各种单向加密，
 */
private fun ByteArray.digest(type: String, from: Int, to: Int): ByteArray {
    val md = MessageDigest.getInstance(type)
    md.update(this, from, to)
    return md.digest()
}

fun ByteArray.md5(from: Int = 0, to: Int = size): ByteArray = digest("MD5", from, to)
fun ByteArray.sha1(from: Int = 0, to: Int = size): ByteArray = digest("SHA1", from, to)
fun ByteArray.sha256(from: Int = 0, to: Int = size): ByteArray = digest("SHA256", from, to)

fun String.md5(): ByteArray = toByteArray().md5()
fun String.sha1(): ByteArray = toByteArray().sha1()
fun String.sha256(): ByteArray = toByteArray().sha256()

/**
 * 最后两个参数，是this的范围，iv太小，没必要这样定范围，需要直接new一个，
 */
fun ByteArray.cipherEncrypt(iv: ByteArray, method: String, mode: String, padding: String, from: Int = 0, to: Int = size): ByteArray {
    val algorithm = "$method/$mode/$padding"
    val key = SecretKeySpec(iv, method)
    val cipher = Cipher.getInstance(algorithm)
    cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(iv))
    return cipher.doFinal(this, from, to)
}

/**
 * 最后两个参数，是this的范围，key和iv太小，没必要这样定范围，需要直接new一个，
 */
fun ByteArray.cipherDecrypt(key: ByteArray, iv: ByteArray, method: String, mode: String, padding: String, from: Int = 0, to: Int = size): ByteArray {
    val algorithm = "$method/$mode/$padding"
    val secretKey = SecretKeySpec(key, method)
    val cipher = Cipher.getInstance(algorithm)
    cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
    return cipher.doFinal(this, from, to)
}

fun String.hex(): String = toByteArray().hex()

fun ByteArray.hex(from: Int = 0, to: Int = size): String {
    val bytes = if (from == 0 && to == size) {
        this
    } else {
        copyOfRange(from, to)
    }
    return bytes.joinToString("") {
        "%02x".format(it)
    }
}

fun ByteArray.base64(urlSafe: Boolean, from: Int = 0, to: Int = size): String {
    val bytes = if (from == 0 && to == size) {
        this
    } else {
        copyOfRange(from, to)
    }
    return if (urlSafe) {
        Base64.encodeUrl(bytes)
    } else {
        Base64.encode(bytes)
    }
}

fun String.base64Decode(): ByteArray {
    return Base64.decode(this)
}
