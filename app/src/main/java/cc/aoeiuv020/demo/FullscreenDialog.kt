package cc.aoeiuv020.demo

import android.app.Dialog
import android.content.Context

class FullscreenDialog(ctx: Context) : Dialog(ctx, R.style.full_dialog_style) {
    companion object {
        fun start(ctx: Context) {
            val dialog = FullscreenDialog(ctx)
            dialog.show()
        }
    }

    init {
        setContentView(layoutInflater.inflate(R.layout.dialog_fullscreen, null))
    }
}