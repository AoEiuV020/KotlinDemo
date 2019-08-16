package cc.aoeiuv020

import android.animation.AnimatorSet
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


        val animSet = AnimatorSet().apply {
            duration = 1000
            play(ValueAnimator.ofInt(80, 240).apply {
                addUpdateListener {
                    val v = it.animatedValue as Int
                    ivCircle.layoutParams = ivCircle.layoutParams.apply {
                        height = dip(v)
                        width = dip(v)
                    }
                }
            }).with(ValueAnimator.ofFloat(1f, 0f).apply {
                addUpdateListener {
                    val v = it.animatedValue as Float
                    ivCircle.alpha = v
                }
            })
        }

        ivCircle.setOnClickListener {
            animSet.start()
        }
    }
}
