package cc.aoeiuv020

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_animation.*
import org.jetbrains.anko.dip
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

        val animator = ValueAnimator.ofInt(80, 160).apply {
            duration = 1000
            repeatCount = 1
            repeatMode = ValueAnimator.REVERSE
            addUpdateListener {
                val v = it.animatedValue as Int
                ivCircle.layoutParams = ivCircle.layoutParams.apply {
                    height = dip(v)
                    width = dip(v)
                }
            }
        }

        ivCircle.setOnClickListener {
            animator.start()
        }
    }
}
