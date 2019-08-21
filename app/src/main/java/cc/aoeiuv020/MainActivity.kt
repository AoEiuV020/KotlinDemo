package cc.aoeiuv020

import android.Manifest
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {
    companion object {
        @Suppress("unused")
        fun start(ctx: Context) {
            ctx.startActivity<MainActivity>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.RECORD_AUDIO
        ), 1)

        btnHello.setOnClickListener {
            toast("Hello")
        }

        btnAudioRecord.setOnClickListener {
            AudioRecordActivity.start(this)
        }
    }
}
