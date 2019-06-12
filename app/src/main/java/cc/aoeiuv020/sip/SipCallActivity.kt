package cc.aoeiuv020.sip

import android.content.Context
import android.net.sip.SipManager
import android.net.sip.SipProfile
import android.net.sip.SipRegistrationListener
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import cc.aoeiuv020.R
import kotlinx.android.synthetic.main.activity_sip_call.*
import org.jetbrains.anko.*

class SipCallActivity : AppCompatActivity(), AnkoLogger {
    companion object {
        fun start(ctx: Context) {
            ctx.startActivity<SipCallActivity>()
        }

    }

    private lateinit var sipManager: SipManager
    private var mMySipProfile: SipProfile? = null

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
                if (username.isNotEmpty() && server.isNotEmpty()) {
                    val uriString = SipProfile.Builder(username, server)
                            .build()
                            .uriString
                    tvRemoteUrl.text = uriString
                }
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

        btnClose.setOnClickListener {
            doAsync({ t ->
                error("关闭sip失败", t)
            }, {
                closeSip(mMySipProfile)
            })
            finish()
        }
    }


    override fun onResume() {
        super.onResume()

        doAsync({ t ->
            error("注册sip失败", t)
        }, {
            val mySipProfile = SipHelper.getMySipProfile(ctx)
            if (mySipProfile == null) {
                SipConfigActivity.start(ctx)
            } else {
                if (mMySipProfile != null && !SipHelper.equals(mMySipProfile, mySipProfile)) {
                    closeSip(mMySipProfile)
                }
                mMySipProfile = mySipProfile
                uiThread {
                    etServer.setText(mySipProfile.sipDomain)
                }
                if (!sipManager.isOpened(mySipProfile.uriString)) {
                    sipManager.open(mySipProfile, SipIncomingCallActivity.pendingIntent(ctx), object : SipRegistrationListener {
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
                            // sip关闭时会回调这个失败，是正常情况，
                        }
                    })
                } else {
                    // 这个打开状态应该是记录在系统里的，客户端直接杀掉不关闭的话下次还是已经打开状态，
                    // 但是PendingIntent依然有效，所以没关系，
                    // 甚至客户端杀掉后还能接到电话，所以结束页面时不需要关闭sip,
                    warn { "already opened: ${mySipProfile.uriString}" }
                }
            }
        })
    }

    /**
     * 这个关闭真的慢，大概要一秒，
     */
    @WorkerThread
    private fun closeSip(sipProfile: SipProfile?) {
        sipProfile?.uriString?.also {
            if (sipManager.isOpened(it)) {
                sipManager.close(it)
            }
        }
    }
}
