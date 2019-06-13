package cc.aoeiuv020.sip

import android.content.Context
import android.net.sip.SipManager
import android.net.sip.SipProfile
import org.jetbrains.anko.defaultSharedPreferences

object SipHelper {
    fun save(
            ctx: Context,
            username: String,
            password: String,
            server: String,
            port: Int = 5060
    ) {
        ctx.defaultSharedPreferences.edit()
                .putString("username", username)
                .putString("password", password)
                .putString("server", server)
                .putInt("port", port)
                .apply()
    }

    fun getMySipProfile(ctx: Context): SipProfile? {
        val sp = ctx.defaultSharedPreferences
        val username = sp.getString("username", null) ?: return null
        val password = sp.getString("password", null) ?: return null
        val server = sp.getString("server", null) ?: return null
        val port = sp.getInt("port", 5060)
        return SipProfile.Builder(username, server)
                .setPort(port)
                .setPassword(password)
                .build()
    }

    fun getSipManager(ctx: Context): SipManager {
        return SipManager.newInstance(ctx) ?: throw IllegalStateException("设备不支持sip")
    }


    fun equals(a: SipProfile?, b: SipProfile?): Boolean {
        if (a == null && b == null) {
            return true
        }
        if (a == null || b == null) {
            return false
        }
        return a.uriString == b.uriString
                && a.password == b.password
    }
}
