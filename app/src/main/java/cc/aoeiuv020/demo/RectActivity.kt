package cc.aoeiuv020.demo

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log
import org.jetbrains.anko.startActivity


class RectActivity : AppCompatActivity() {
    companion object {
        fun start(ctx: Context) {
            ctx.startActivity<RectActivity>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rect)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getNotchParams()
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun getNotchParams() {
        val decorView = window.decorView

        decorView.post {
            val displayCutout = decorView.rootWindowInsets.displayCutout ?: return@post
            Log.e("TAG", "安全区域距离屏幕左边的距离 SafeInsetLeft:" + displayCutout.safeInsetLeft)
            Log.e("TAG", "安全区域距离屏幕右部的距离 SafeInsetRight:" + displayCutout.safeInsetRight)
            Log.e("TAG", "安全区域距离屏幕顶部的距离 SafeInsetTop:" + displayCutout.safeInsetTop)
            Log.e("TAG", "安全区域距离屏幕底部的距离 SafeInsetBottom:" + displayCutout.safeInsetBottom)

            val rects = displayCutout.boundingRects
            if (rects == null || rects.size == 0) {
                Log.e("TAG", "不是刘海屏")
            } else {
                Log.e("TAG", "刘海屏数量:" + rects.size)
                for (rect in rects) {
                    Log.e("TAG", "刘海屏区域：$rect")
                }
            }
        }
    }
}
