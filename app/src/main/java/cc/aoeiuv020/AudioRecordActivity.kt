package cc.aoeiuv020

import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_audio_record.*
import org.jetbrains.anko.startActivity
import java.io.IOException


class AudioRecordActivity : AppCompatActivity() {
    companion object {
        @Suppress("unused")
        fun start(ctx: Context) {
            ctx.startActivity<AudioRecordActivity>()
        }

        val RECORDER_SAMPLERATE = 8000
        val RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO
        val RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT

        val BufferElements2Rec = 1024 // want to play 2048 (2K) since 2 bytes we use only 1024
        val BytesPerElement = 2 // 2 bytes in 16bit format
    }

    private var recorder: AudioRecord? = null
    private var recordingThread: Thread? = null
    private var isRecording = false

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_record)

        setButtonHandlers()
        enableButtons(false)
    }

    private fun setButtonHandlers() {
        btnStart.setOnClickListener {
            enableButtons(true)
            startRecording()
        }
        btnStop.setOnClickListener {
            enableButtons(false)
            stopRecording()
        }
    }

    private fun enableButton(id: Int, isEnable: Boolean) {
        (findViewById<View>(id)).isEnabled = isEnable
    }

    private fun enableButtons(isRecording: Boolean) {
        enableButton(R.id.btnStart, !isRecording)
        enableButton(R.id.btnStop, isRecording)
    }

    private fun startRecording() {

        recorder = AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, BufferElements2Rec * BytesPerElement)

        recorder!!.startRecording()
        isRecording = true
        recordingThread = Thread(Runnable { writeAudioDataToFile() }, "AudioRecorder Thread")
        recordingThread!!.start()
    }

    //convert short to byte
    private fun short2byte(sData: ShortArray): ByteArray {
        val shortArrsize = sData.size
        val bytes = ByteArray(shortArrsize * 2)
        for (i in 0 until shortArrsize) {
            bytes[i * 2] = (sData[i].toInt() and 0x00FF).toByte()
            bytes[i * 2 + 1] = (sData[i].toInt() shr 8).toByte()
            sData[i] = 0
        }
        return bytes

    }

    private fun writeAudioDataToFile() {
        // Write the output audio in byte

        val sData = ShortArray(BufferElements2Rec)

        val os = openFileOutput("audioRecord.pcm", Context.MODE_PRIVATE)

        while (isRecording) {
            // gets the voice output from microphone to byte format

            recorder!!.read(sData, 0, BufferElements2Rec)
            println("Short wirting to file$sData")
            try {
                // // writes the data to file from buffer
                // // stores the voice buffer
                val bData = short2byte(sData)
                os.write(bData, 0, BufferElements2Rec * BytesPerElement)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        os.close()

    }

    private fun stopRecording() {
        // stops the recording activity
        if (null != recorder) {
            isRecording = false
            recorder!!.stop()
            recorder!!.release()
            recorder = null
            recordingThread = null
        }
    }
}
