package cc.aoeiuv020

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import me.imid.swipebacklayout.lib.SwipeBackLayout
import me.imid.swipebacklayout.lib.app.SwipeBackActivity
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity

class SwipeBackLayoutActivity : SwipeBackActivity(), AnkoLogger {
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

        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
            fun onAny() {
                info { "a: $number-${lifecycle.currentState}" }
            }
        })

        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT)
    }
}
