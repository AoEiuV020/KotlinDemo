package cc.aoeiuv020.demo

import android.annotation.SuppressLint
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

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid)

        gridView.addHeaderView(layoutInflater.inflate(R.layout.grid_header, null))
        gridView.addFooterView(layoutInflater.inflate(R.layout.grid_footer, null))

        gridView.adapter = MyGridAdapter()
    }
}
