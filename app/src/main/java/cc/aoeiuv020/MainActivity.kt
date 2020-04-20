package cc.aoeiuv020

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {
    companion object {
        @Suppress("unused")
        fun start(ctx: Context) {
            ctx.startActivity<MainActivity>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(this, packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS).requestedPermissions, 1)

        btnHello.setOnClickListener {
            toast("Hello")
        }

        btnStorage.setOnClickListener {
            StorageActivity.start(this)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnDocument.setOnClickListener {
                DocumentActivity.start(this)
            }
        } else {
            btnDocument.visibility = View.GONE
        }

        btnImage.setOnClickListener {
            ImageActivity.start(this)
        }
    }
}
