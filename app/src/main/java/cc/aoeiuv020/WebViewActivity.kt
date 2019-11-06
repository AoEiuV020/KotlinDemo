package cc.aoeiuv020

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_web_view.*
import org.jetbrains.anko.startActivity

class WebViewActivity : AppCompatActivity() {
    companion object {
        @Suppress("unused")
        fun start(ctx: Context) {
            ctx.startActivity<WebViewActivity>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarLight(true)
        setContentView(R.layout.activity_web_view)
        supportActionBar?.hide()

        AndroidBug5497Workaround.assistActivity(this)

        webView.webViewClient = WebViewClient()
        webView.loadUrl("https://www.baidu.com/s?wd=1")
    }

    protected fun setStatusBarLight(light: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // >=5.0 背景为全透明
            /* >=5.0，this method(getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS));
            in some phone is half-transparent like vivo、nexus6p..
            in some phone is full-transparent
            so ...*/
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (light) {
                    window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                } else {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                }
                window.statusBarColor = Color.TRANSPARENT
            } else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                if (light) {
                    window.statusBarColor = Color.BLACK
                } else {
                    window.statusBarColor = Color.TRANSPARENT
                }
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 4.4背景为渐变半透明
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }
}
