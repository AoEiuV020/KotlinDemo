package cc.aoeiuv020

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnOldCamera.setOnClickListener {
            OldCameraActivity.start(this)
        }

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
    }
}
