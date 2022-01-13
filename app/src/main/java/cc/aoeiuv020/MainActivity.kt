package cc.aoeiuv020

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    companion object {
        @Suppress("unused")
        fun start(ctx: Context) {
            ctx.startActivity(Intent(ctx, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS).requestedPermissions?.takeIf {
            it.isNotEmpty()
        }?.let {        
            ActivityCompat.requestPermissions(this, it, 1)
        }

        findViewById<View>(R.id.btnHello).setOnClickListener {
            Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show()
        }
    }
}
