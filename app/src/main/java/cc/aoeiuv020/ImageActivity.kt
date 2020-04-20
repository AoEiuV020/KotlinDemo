package cc.aoeiuv020

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_image.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread
import java.net.URL

class ImageActivity : AppCompatActivity() {
    companion object {
        @Suppress("unused")
        fun start(ctx: Context) {
            ctx.startActivity<ImageActivity>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        ActivityCompat.requestPermissions(this, packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS).requestedPermissions, 1)

        btnDownload.setOnClickListener {
            doAsync {
                URL(getUrl()).openStream().use { input ->
                    openFileOutput("imageCache", Context.MODE_PRIVATE).use { output ->
                        input.copyTo(output)
                    }
                }
                uiThread {
                    ivImage.setImageURI(Uri.fromFile(getFileStreamPath("imageCache")))
                }
            }
        }
    }

    private fun getUrl(): String {
        return etUrl.text.toString()
    }

}
