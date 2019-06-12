package cc.aoeiuv020.sip

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cc.aoeiuv020.R
import kotlinx.android.synthetic.main.activity_sip_call.*
import org.jetbrains.anko.startActivity

class SipCallActivity : AppCompatActivity() {
    companion object {
        fun start(ctx: Context) {
            ctx.startActivity<SipCallActivity>()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sip_call)

        btnConfig.setOnClickListener {
            SipConfigActivity.start(this)
        }
    }
}
