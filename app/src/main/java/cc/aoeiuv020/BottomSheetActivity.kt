package cc.aoeiuv020

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cc.aoeiuv020.anull.notNull
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_bottom_sheet.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class BottomSheetActivity : AppCompatActivity() {
    companion object {
        fun start(ctx: Context) {
            ctx.startActivity<BottomSheetActivity>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_sheet)

        btnBottomSheetDialog.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            dialog.setContentView(R.layout.dialog_hello)
            dialog.findViewById<View>(R.id.btnHello).notNull().setOnClickListener {
                toast("Hello")
            }
            dialog.show()
        }
    }
}
