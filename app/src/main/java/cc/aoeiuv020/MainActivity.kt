package cc.aoeiuv020

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.startActivity

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
    }
}
