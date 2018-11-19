package cc.aoeiuv020.demo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_browser.*
import org.jetbrains.anko.startActivity
import java.lang.ref.SoftReference

class BrowserActivity : AppCompatActivity() {
    companion object {
        const val KEY_URL = "url"
        @SuppressLint("StaticFieldLeak")
        private var softWebView: SoftReference<WebView?> = SoftReference(null)

        fun start(ctx: Context, url: String) {
            ctx.startActivity<BrowserActivity>(KEY_URL to url)
        }

        @SuppressLint("SetJavaScriptEnabled")
        fun getWebView(ctx: Context): WebView = softWebView.get()
                ?: WebView(ctx.applicationContext)
                        .also { softWebView = SoftReference(it) }
                        .apply {
                            webViewClient = WebViewClient()
                            settings.javaScriptEnabled = true
                            /* 设置为true表示支持使用js打开新的窗口 */
                            settings.javaScriptCanOpenWindowsAutomatically = true

                            /* 设置缓存模式 */
                            settings.cacheMode = WebSettings.LOAD_DEFAULT
                            settings.domStorageEnabled = true

                            /* 设置为使用webview推荐的窗口 */
                            settings.useWideViewPort = true
                            /* 设置为使用屏幕自适配 */
                            settings.loadWithOverviewMode = true
                            /* 设置是否允许webview使用缩放的功能,我这里设为false,不允许 */
                            settings.builtInZoomControls = false
                            /* 提高网页渲染的优先级 */
                            @Suppress("DEPRECATION")
                            settings.setRenderPriority(WebSettings.RenderPriority.HIGH)

                            /* HTML5的地理位置服务,设置为true,启用地理定位 */
                            settings.setGeolocationEnabled(true)
                            /* 设置可以访问文件 */
                            settings.allowFileAccess = true
                        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)

        val wv = getWebView(this)
        (wv.parent as? ViewGroup)?.removeView(wv)
        flContent.addView(wv)

        intent?.let {
            init(it)
        }
    }

    private fun init(intent: Intent) {
        val wv = getWebView(this)
        intent.getStringExtra(KEY_URL)?.let { url ->
            if (url != wv.tag) {
                wv.tag = url
                wv.loadUrl(url)
            }
        }
    }
}
