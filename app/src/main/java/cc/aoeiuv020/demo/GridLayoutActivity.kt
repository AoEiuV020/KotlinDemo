package cc.aoeiuv020.demo

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_grid_layout.*
import org.jetbrains.anko.startActivity

class GridLayoutActivity : AppCompatActivity() {
    companion object {
        fun start(ctx: Context) {
            ctx.startActivity<GridLayoutActivity>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid_layout)

        repeat(24) {
            val view = TextView(this)
            view.text = it.toString()
            gridLayout.addView(view, it)
        }
    }
}
