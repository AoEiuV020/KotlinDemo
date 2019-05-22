@file:Suppress("DEPRECATION")

package cc.aoeiuv020

import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.os.Bundle
import android.view.TextureView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_old_camera.*
import org.jetbrains.anko.startActivity

class OldCameraActivity : AppCompatActivity() {
    companion object {
        fun start(ctx: Context) {
            ctx.startActivity<OldCameraActivity>()
        }
    }

    private var mCamera: Camera? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_old_camera)

        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {

            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                val camera = Camera.open(0).also { mCamera = it }
                camera.setPreviewTexture(surface)
                camera.startPreview()
                val cameraInfo = Camera.CameraInfo()
                Camera.getCameraInfo(0, cameraInfo)
            }

            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                mCamera?.run {
                    stopPreview()
                    release()
                }
                mCamera = null
                return true
            }
        }
    }
}
