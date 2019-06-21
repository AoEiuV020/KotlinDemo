package cc.aoeiuv020

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.startActivity

class KeyboardHeightActivity : AppCompatActivity() {
    companion object {
        fun start(ctx: Context) {
            ctx.startActivity<KeyboardHeightActivity>()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keyboard_height)
    }
}
