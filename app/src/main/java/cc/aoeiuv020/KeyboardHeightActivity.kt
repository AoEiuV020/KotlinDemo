package cc.aoeiuv020

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
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

    private val hideBottomTalk = Runnable {
        hideBottomView()
        updateSoftInputMethod(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

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
            if (isBottomViewShowing()) {
                toggleSoftInput()
                vBottom.postDelayed(hideBottomTalk, 500)
            } else {
                vBottom.removeCallbacks(hideBottomTalk)
                updateSoftInputMethod(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
                showBottomView()
                hideKeyboard()
            }
        }
        editText.setOnClickListener {
            if (isBottomViewShowing()) {
                vBottom.postDelayed(hideBottomTalk, 500)
            }
        }

        rvContent.layoutManager = LinearLayoutManager(this)
        val adapter = NumberListAdapter()
        rvContent.adapter = adapter
        rvContent.scrollToPosition(adapter.itemCount)
    }

    private fun isBottomViewShowing(): Boolean {
        return vBottom.visibility == View.VISIBLE
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

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }


    private fun toggleSoftInput() {
        val imm = editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun updateSoftInputMethod(softInputMode: Int) {
        if (isFinishing) {
            return
        }
        val p = window.attributes
        if (p.softInputMode == softInputMode) {
            return
        }
        p.softInputMode = softInputMode
        window.attributes = p
    }
}
