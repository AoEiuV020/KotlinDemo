package cc.aoeiuv020.demo.adb

import android.content.Context
import com.cgutman.adblib.AdbCrypto

/**
 * Created by AoEiuV020 on 2019.02.01-14:23:43.
 */
object AdbManager {
    lateinit var crypto: AdbCrypto
    fun init(ctx: Context) {
        crypto = AdbUtils.readCryptoConfig(ctx.filesDir)
                ?: AdbUtils.writeNewCryptoConfig(ctx.filesDir)
    }

    fun loadAdbCrypto(): AdbCrypto {
        return crypto
    }
}