package cc.aoeiuv020.sip

import android.content.Context
import android.net.sip.SipProfile
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import cc.aoeiuv020.R
import kotlinx.android.synthetic.main.activity_sip_call.*
import org.jetbrains.anko.startActivity

class SipCallActivity : AppCompatActivity() {
    companion object {
        fun start(ctx: Context) {
            ctx.startActivity<SipCallActivity>()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sip_call)

        btnConfig.setOnClickListener {
            SipConfigActivity.start(this)
        }
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val username = etUsername.text.toString()
                val server = etServer.text.toString()
                val uriString = SipProfile.Builder(username, server)
                        .build()
                        .uriString
                tvRemoteUrl.text = uriString
            }
        }
        etUsername.addTextChangedListener(textWatcher)
        etServer.addTextChangedListener(textWatcher)
    }


    override fun onResume() {
        super.onResume()

        val me = SipHelper.getMySipProfile(this)
        if (me == null) {
            SipConfigActivity.start(this)
        } else {
            etServer.setText(me.sipDomain)
        }
    }
}
