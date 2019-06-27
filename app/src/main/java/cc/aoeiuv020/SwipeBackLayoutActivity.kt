package cc.aoeiuv020

import android.content.Context
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_swipe_back_layout.*
import me.imid.swipebacklayout.lib.SwipeBackLayout
import me.imid.swipebacklayout.lib.app.SwipeBackActivity
import org.jetbrains.anko.startActivity

class SwipeBackLayoutActivity : SwipeBackActivity() {
    companion object {
        fun start(ctx: Context, number: Int) {
            ctx.startActivity<SwipeBackLayoutActivity>("number" to number)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe_back_layout)

        val number = intent.getIntExtra("number", 0)
        title = number.toString()

        btnNewPage.setOnClickListener {
            start(this, number + 1)
        }

        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT)
    }
}
