package cc.aoeiuv020.sip

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.sip.SipAudioCall
import android.net.sip.SipSession
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cc.aoeiuv020.R
import kotlinx.android.synthetic.main.activity_sip_incoming_call.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import java.util.*

class SipIncomingCallActivity : AppCompatActivity(), AnkoLogger {
    companion object {
        fun pendingIntent(ctx: Context): PendingIntent {
            val intent = ctx.intentFor<SipIncomingCallActivity>()
            return PendingIntent.getActivity(ctx, 1, intent, Intent.FILL_IN_DATA)
        }
    }

    private lateinit var sipAudioCall: SipAudioCall

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sip_incoming_call)

        info { "intent: $intent" }
        logBundle(intent.extras)

        val sipManager = SipHelper.getSipManager(this)
        sipAudioCall = sipManager.takeAudioCall(intent, object : SipAudioCall.Listener() {

            // 接通后对方挂断的情况，
            override fun onCallEnded(call: SipAudioCall) {
                super.onCallEnded(call)
                closeSip()
            }

            override fun onChanged(call: SipAudioCall) {
                info {
                    "${Thread.currentThread().stackTrace[3].methodName}, " +
                            "state: ${SipSession.State.toString(call.state)}"
                }
                info { "income onChanged thread: ${Thread.currentThread().name}" }
                runOnUiThread {
                    tvStatus.text = SipSession.State.toString(call.state)
                }
            }
        })

        btnHangUp.setOnClickListener {
            closeSip()
        }
        btnTake.setOnClickListener {
            sipAudioCall.apply {
                answerCall(30)
                startAudio()
                setSpeakerMode(true)
                if (isMuted) {
                    toggleMute()
                }
            }
            btnTake.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        sipAudioCall.endCall()
        sipAudioCall.close()
        super.onDestroy()
    }

    private fun closeSip() {
        sipAudioCall.endCall()
        sipAudioCall.close()
        finish()
    }

    private fun logBundle(bundle: Bundle?) {
        val sb = StringBuilder()
        sb.append("bundle = ")
        if (bundle == null) {
            sb.append("null").append('\n')
        } else {
            val map = HashMap<String, Any>()
            for (key in bundle.keySet()) {
                val value = bundle.get(key)
                map[key] = value!!
            }
            sb.append(map.toString())
        }
        info { sb }
    }
}
