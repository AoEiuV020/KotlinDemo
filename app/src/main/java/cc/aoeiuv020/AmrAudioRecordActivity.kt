package cc.aoeiuv020

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cc.aoeiuv020.audio.AmrEncoder
import cc.aoeiuv020.audio.AudioRecorder
import kotlinx.android.synthetic.main.activity_audio_record.*
import org.jetbrains.anko.startActivity


class AmrAudioRecordActivity : AppCompatActivity() {
    companion object {
        @Suppress("unused")
        fun start(ctx: Context) {
            ctx.startActivity<AmrAudioRecordActivity>()
        }
    }

    private lateinit var audioRecorder: AudioRecorder

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_record)

        initRecorder()

        btnStart.setOnClickListener {
            audioRecorder.startRecord()
        }

        btnStop.setOnClickListener {
            audioRecorder.stop()
        }
    }

    private fun initRecorder() {
        audioRecorder = AudioRecorder(object : AudioRecorder.CallBack {
            @SuppressLint("SetTextI18n")
            override fun recordProgress(progress: Int) {
                tvProgress.post { tvProgress.text = "" + progress }
            }

            @SuppressLint("SetTextI18n")
            override fun volumn(volumn: Int) {
                tvVolume.post { tvVolume.text = "" + volumn }
            }
        }, AmrEncoder())
    }
}
