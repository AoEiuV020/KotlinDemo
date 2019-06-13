package cc.aoeiuv020.sip

import android.annotation.SuppressLint
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
import cc.aoeiuv020.anull.notNull
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
                val mySipProfile = mMySipProfile ?: return
                val username = etUsername.text.toString()
                if (username.isNotEmpty()) {
                    val uriString = SipProfile.Builder(username, mySipProfile.sipDomain)
                            .setPort(mySipProfile.port)
                            .build()
                            .uriString
                    tvRemoteUrl.text = uriString
                }
            }
        }
        etUsername.addTextChangedListener(textWatcher)
        defaultSharedPreferences.getString("peerUsername", null)?.also {
            etUsername.setText(it)
        }

        sipManager = SipHelper.getSipManager(this)

        btnCall.setOnClickListener {
            val username = etUsername.text.toString()
            val mySipProfile = mMySipProfile.notNull()
            val peerProfile = SipProfile.Builder(username, mySipProfile.sipDomain)
                    .setPort(mySipProfile.port)
                    .build()
            info { peerProfile.uriString }
            defaultSharedPreferences.edit()
                    .putString("peerUsername", username)
                    .apply()
            SipMakeCallActivity.start(this, peerProfile)
        }

        btnClose.setOnClickListener {
            doAsync({ t ->
                error("关闭sip失败", t)
            }, {
                mMySipProfile?.also { closeSip(it) }
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
                mMySipProfile?.also {
                    if (!SipHelper.equals(it, mySipProfile)) {
                        closeSip(it)
                    }
                }
                mMySipProfile = mySipProfile
                if (!sipManager.isOpened(mySipProfile.uriString)) {
                    sipManager.open(mySipProfile, SipIncomingCallActivity.pendingIntent(ctx), object : SipRegistrationListener {
                        @SuppressLint("SetTextI18n")
                        override fun onRegistering(localProfileUri: String) {
                            info { localProfileUri }
                            uiThread {
                                tvStatus.text = "onRegistering"
                            }
                        }

                        @SuppressLint("SetTextI18n")
                        override fun onRegistrationDone(localProfileUri: String, expiryTime: Long) {
                            info { "localProfileUri: $localProfileUri, expiryTime: $expiryTime" }
                            uiThread {
                                tvStatus.text = "onRegistrationDone"
                            }
                        }

                        @SuppressLint("SetTextI18n")
                        override fun onRegistrationFailed(localProfileUri: String, errorCode: Int, errorMessage: String?) {
                            info {
                                "localProfileUri: $localProfileUri, " +
                                        "errorCode: $errorCode, " +
                                        "errorMessage: $errorMessage"
                            }
                            // sip关闭时会回调这个失败，是正常情况，
                            uiThread {
                                tvStatus.text = "onRegistrationFailed"
                            }
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
    private fun closeSip(sipProfile: SipProfile) {
        sipProfile.uriString.also {
            if (sipManager.isOpened(it)) {
                sipManager.close(it)
            }
        }
    }
}
