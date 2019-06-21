package cc.aoeiuv020

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_keyboard_height.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity

class KeyboardHeightActivity : AppCompatActivity(), AnkoLogger {
    companion object {
        fun start(ctx: Context) {
            ctx.startActivity<KeyboardHeightActivity>()
        }
    }

    private var mKeyboardHeight = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keyboard_height)

        SoftKeyBoardListener.setListener(this, object : SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
            override fun keyBoardShow(height: Int) {
                info { "show $height" }
                mKeyboardHeight = height
            }

            override fun keyBoardHide(height: Int) {
                info { "hide $height" }
                mKeyboardHeight = height
            }
        })

        btnToggleView.setOnClickListener {
            if (vBottom.visibility == View.GONE) {
                showBottomView()
            } else {
                hideBottomView()
            }
        }
    }

    private fun hideBottomView() {
        vBottom.visibility = View.GONE
    }

    private fun showBottomView() {
        vBottom.visibility = View.VISIBLE
        val height = mKeyboardHeight.takeIf { it > 0 }
                ?: 200
        if (vBottom.height != height) {
            vBottom.layoutParams = vBottom.layoutParams.also {
                it.height = height
            }
        }
    }
}
