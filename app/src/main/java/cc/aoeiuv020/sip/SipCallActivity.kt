package cc.aoeiuv020.sip

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.sip.SipManager
import android.net.sip.SipProfile
import android.net.sip.SipRegistrationListener
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import cc.aoeiuv020.R
import kotlinx.android.synthetic.main.activity_sip_call.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity

class SipCallActivity : AppCompatActivity(), AnkoLogger {
    companion object {
        fun start(ctx: Context) {
            ctx.startActivity<SipCallActivity>()
        }

    }

    private lateinit var sipManager: SipManager

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

        sipManager = SipHelper.getSipManager(this)

        btnCall.setOnClickListener {
            val username = etUsername.text.toString()
            val server = etServer.text.toString()
            val peerProfile = SipProfile.Builder(username, server)
                    .build()
            info { peerProfile.uriString }
        }
    }


    override fun onResume() {
        super.onResume()

        val me = SipHelper.getMySipProfile(this)
        if (me == null) {
            SipConfigActivity.start(this)
        } else {
            etServer.setText(me.sipDomain)
            if (!sipManager.isOpened(me.uriString)) {
                val intent = intentFor<SipCallingActivity>()
                val pi = PendingIntent.getActivity(this, 1, intent, Intent.FILL_IN_DATA)
                sipManager.open(me, pi, object : SipRegistrationListener {
                    override fun onRegistering(localProfileUri: String) {
                        info { localProfileUri }
                    }

                    override fun onRegistrationDone(localProfileUri: String, expiryTime: Long) {
                        info { "localProfileUri: $localProfileUri, expiryTime: $expiryTime" }
                    }

                    override fun onRegistrationFailed(localProfileUri: String, errorCode: Int, errorMessage: String?) {
                        info {
                            "localProfileUri: $localProfileUri, " +
                                    "errorCode: $errorCode, " +
                                    "errorMessage: $errorMessage"
                        }
                    }
                })
            }
        }
    }
}
