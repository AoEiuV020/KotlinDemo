package cc.aoeiuv020.sip

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cc.aoeiuv020.R
import org.jetbrains.anko.startActivity

class SipConfigActivity : AppCompatActivity() {
    companion object {
        fun start(ctx: Context) {
            ctx.startActivity<SipConfigActivity>()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sip_config)
    }
}
