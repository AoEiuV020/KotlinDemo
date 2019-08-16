package cc.aoeiuv020

import android.content.Context
import android.os.Bundle
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_animation.*
import org.jetbrains.anko.startActivity

class AnimationActivity : AppCompatActivity() {
    companion object {
        fun start(ctx: Context) {
            ctx.startActivity<AnimationActivity>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)

        val anim = AlphaAnimation(1f, 0f)
        anim.duration = 1000
        ivCircle.setOnClickListener {
            ivCircle.startAnimation(anim)
        }
    }
}
