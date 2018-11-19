package cc.aoeiuv020.demo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_browser.*
import org.jetbrains.anko.startActivity

class BrowserActivity : AppCompatActivity() {
    companion object {
        val KEY_URL = "url"
        fun start(ctx: Context, url: String) {
            ctx.startActivity<BrowserActivity>(KEY_URL to url)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)

        initWebView()


        intent?.let {
            init(it)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        /* 设置支持Js */
        wvBrowser.run {
            webViewClient = CustomClient()
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

    private fun init(intent: Intent) {
        intent.getStringExtra(KEY_URL)?.let {
            open(it)
        }
    }

    private fun open(url: String) {
        if (url != wvBrowser.tag) {
            wvBrowser.tag = url
            wvBrowser.loadUrl(url)
        }
    }

    override fun finish() {
        // 整个app都到后台了，不只有这个activity,
        moveTaskToBack(true)
    }

    class CustomClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return false
        }
    }
}
