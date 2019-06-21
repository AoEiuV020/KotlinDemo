package cc.aoeiuv020

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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
    private var keyboardHideCallback: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keyboard_height)

        SoftKeyBoardListener.setListener(this, object : SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
            override fun keyBoardShow(height: Int) {
                info { "show $height" }
                mKeyboardHeight = height
                hideBottomView()
            }

            override fun keyBoardHide(height: Int) {
                info { "hide $height" }
                mKeyboardHeight = height
                keyboardHideCallback?.also {
                    keyboardHideCallback = null
                    runOnUiThread(it)
                }
            }
        })

        btnToggleView.setOnClickListener {
            if (vBottom.visibility == View.GONE) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(editText.windowToken, 0)
                keyboardHideCallback = Runnable {
                    showBottomView()
                }
            } else {
                hideBottomView()
            }
        }

        rvContent.layoutManager = LinearLayoutManager(this)
        val adapter = NumberListAdapter()
        rvContent.adapter = adapter
        rvContent.scrollToPosition(adapter.itemCount)
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
