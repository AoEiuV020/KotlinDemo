package cc.aoeiuv020

import android.Manifest.permission.USE_SIP
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import cc.aoeiuv020.sip.SipCallActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(this, arrayOf(
                USE_SIP
        ), 1)

        btnSip.setOnClickListener {
            SipCallActivity.start(this)
        }
    }
}
