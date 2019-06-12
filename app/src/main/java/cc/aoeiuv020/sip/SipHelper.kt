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
            server: String
    ) {
        ctx.defaultSharedPreferences.edit()
                .putString("username", username)
                .putString("password", password)
                .putString("server", server)
                .apply()
    }

    fun getMySipProfile(ctx: Context): SipProfile? {
        val sp = ctx.defaultSharedPreferences
        val username = sp.getString("username", null) ?: return null
        val password = sp.getString("password", null) ?: return null
        val server = sp.getString("server", null) ?: return null
        return SipProfile.Builder(username, server)
                .setPassword(password)
                .build()
    }

    fun getSipManager(ctx: Context): SipManager {
        return SipManager.newInstance(ctx)
    }
}
