package cc.aoeiuv020

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_storage.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.File
import java.util.*

class StorageActivity : AppCompatActivity() {
    companion object {
        @Suppress("unused")
        fun start(ctx: Context) {
            ctx.startActivity<StorageActivity>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage)

        ActivityCompat.requestPermissions(this, packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS).requestedPermissions, 1)

        btnRead.setOnClickListener {
            toast(File(etPath.text.toString()).readText())
        }

        btnWrite.setOnClickListener {
            val f = File(etPath.text.toString())
            f.parentFile?.takeIf { !it.exists() }
                    ?.mkdirs()
            f.writeText("android q ${Date()}")
        }
    }
}
