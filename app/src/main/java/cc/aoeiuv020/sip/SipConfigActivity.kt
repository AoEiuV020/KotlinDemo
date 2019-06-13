package cc.aoeiuv020.sip

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cc.aoeiuv020.R
import kotlinx.android.synthetic.main.activity_sip_config.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.startActivity

class SipConfigActivity : AppCompatActivity() {
    companion object {
        fun start(ctx: Context) {
            ctx.startActivity<SipConfigActivity>()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sip_config)

        btnSave.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            val server = etServer.text.toString()
            val port = etPort.text.toString().toInt()
            SipHelper.save(this, username, password, server, port)
            finish()
        }
        val sp = ctx.defaultSharedPreferences
        val username = sp.getString("username", null)
        val password = sp.getString("password", null)
        val server = sp.getString("server", null)
        val port = sp.getInt("port", 5060)

        etUsername.setText(username)
        etPassword.setText(password)
        etServer.setText(server)
        etPort.setText(port.toString())
    }
}
