package cc.aoeiuv020.demo

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_grid_recycler.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.startActivity

class GridRecyclerActivity : AppCompatActivity() {
    companion object {
        fun start(ctx: Context) {
            ctx.startActivity<GridRecyclerActivity>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid_recycler)

        recyclerView.run {
            adapter = MyRecyclerAdapter()
            layoutManager = GridLayoutManager(ctx, 8)
        }

    }
}
