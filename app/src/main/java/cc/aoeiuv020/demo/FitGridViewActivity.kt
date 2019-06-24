package cc.aoeiuv020.demo

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_fit_grid_view.*
import org.jetbrains.anko.startActivity

class FitGridViewActivity : AppCompatActivity() {
    companion object {
        fun start(ctx: Context) {
            ctx.startActivity<FitGridViewActivity>()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fit_grid_view)

        fitGridView.setFitGridAdapter(MyFitGridAdapter(this))
    }
}
