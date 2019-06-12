package cc.aoeiuv020.sip

import android.net.sip.SipAudioCall
import android.net.sip.SipSession
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cc.aoeiuv020.R
import kotlinx.android.synthetic.main.activity_sip_calling.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class SipCallingActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var sipAudioCall: SipAudioCall

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sip_calling)

        info { "intent: $intent" }
        info { "bundle: ${intent.extras}" }

        val sipManager = SipHelper.getSipManager(this)
        sipAudioCall = sipManager.takeAudioCall(intent, object : SipAudioCall.Listener() {
            override fun onChanged(call: SipAudioCall) {
                info { "state: ${SipSession.State.toString(call.state)}" }
            }
        })

        btnHangUp.setOnClickListener {
            sipAudioCall.endCall()
            sipAudioCall.close()
            finish()
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
}
