package cc.aoeiuv020

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cc.aoeiuv020.sip.SipCallActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSip.setOnClickListener {
            SipCallActivity.start(this)
        }
    }
}