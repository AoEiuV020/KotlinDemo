package cc.aoeiuv020.demo

import android.Manifest
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import cc.aoeiuv020.demo.call.VideoCallActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.ctx

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCall.setOnClickListener {
            VideoCallActivity.start(ctx)
        }

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA), 0)
    }
}
