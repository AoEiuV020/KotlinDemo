package cc.aoeiuv020

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.leon.channel.reader.IdValueReader
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import java.io.File


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

        btnHello.setOnClickListener {
            alert(IdValueReader.getStringValueById(File(applicationInfo.sourceDir), 0x12345678)
                    ?: "null") {
            }.show()
        }
    }
}
