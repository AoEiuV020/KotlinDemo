package cc.aoeiuv020.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setText(etText.text.toString())
        setTextSize(sbSize.progress.toFloat())
        etText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                setText(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        sbSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                setTextSize(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun setTextSize(progress: Float) {
        // 实测，默认设置下，sp和dp一样大，
        tvPx.setTextSize(TypedValue.COMPLEX_UNIT_PX, progress)
        tvDp.setTextSize(TypedValue.COMPLEX_UNIT_DIP, progress)
        tvSp.setTextSize(TypedValue.COMPLEX_UNIT_SP, progress)
    }

    private fun setText(s: String) {
        listOf(tvPx, tvDp, tvSp).forEach { tv ->
            tv.text = s
        }
    }
}
