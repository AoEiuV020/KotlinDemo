package cc.aoeiuv020

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
        setContentView(WebView(this))
        App.initLanguage(this)

        ActivityCompat.requestPermissions(this, packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS).requestedPermissions, 1)

        setTitle(R.string.title_web_view)
    }
}
