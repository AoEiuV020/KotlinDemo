package cc.aoeiuv020.demo

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_grid.*
import org.jetbrains.anko.startActivity

class GridActivity : AppCompatActivity() {
    companion object {
        fun start(ctx: Context) {
            ctx.startActivity<GridActivity>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid)

        gridView.adapter = MyGridAdapter()
    }
}
