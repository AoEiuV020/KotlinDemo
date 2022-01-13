@file:Suppress("DEPRECATION")

package cc.aoeiuv020

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import cc.aoeiuv020.anull.notNull
import kotlin.properties.Delegates


class OldCameraActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "OldCameraActivity"
        fun start(ctx: Context) {
            ctx.startActivity(Intent(ctx, OldCameraActivity::class.java))
        }
    }
    private val ivPreview by lazy { findViewById<ImageView>(R.id.ivPreview) }
    private val textureView by lazy { findViewById<TextureView>(R.id.textureView) }

    private var mCamera: Camera? = null
    private var mCameraId: Int = 0

    private var mWidth: Int by Delegates.notNull()
    private var mHeight: Int by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_old_camera)

        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {

            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                mWidth = width
                mHeight = height
                openCamera()
            }

            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                releaseCamera()
                return true
            }
        }

        textureView.setOnClickListener {
            switchCamera()
        }

        val btnTakePhoto = findViewById<View>(R.id.btnTakePhoto)
        btnTakePhoto.setOnClickListener {
            val camera = mCamera ?: return@setOnClickListener
            camera.takePicture(null, null, { data: ByteArray, _: Camera ->
                val info = CameraUtils.getCameraInfo(mCameraId)
                var bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                bitmap = CameraUtils.restoreRotatedImage(info.orientation, bitmap)
                ivPreview.visibility = View.VISIBLE
                ivPreview.setImageBitmap(bitmap)
            })
        }
    }

    override fun onBackPressed() {
        if (ivPreview.isShown) {
            ivPreview.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }

    private fun openCamera() {
        openCamera(mCameraId)
    }


    private fun openCamera(id: Int) {
        mCameraId = id
        releaseCamera()
        initCamera(Camera.open(id))
    }

    private fun switchCamera() {
        val count = Camera.getNumberOfCameras()
        val next = (mCameraId + 1) % count
        openCamera(next)
    }

    private fun initCamera(camera: Camera) {
        mCamera = camera
        val surface: SurfaceTexture = textureView.surfaceTexture.notNull()
        camera.setPreviewTexture(surface)
        val cameraInfo = Camera.CameraInfo()
        Camera.getCameraInfo(0, cameraInfo)
        val rotation = getCameraDisplayOrientation(cameraInfo);
        camera.setDisplayOrientation(rotation)
        Log.i(TAG, "rotation <${rotation}>")
        camera.parameters = camera.parameters.apply {
            val previewSize =
                CameraUtils.getOptimalPreviewSize(supportedPreviewSizes, mWidth, mHeight)
            Log.i(TAG, "previewSize <${previewSize.width}, ${previewSize.height}>")
            setPreviewSize(previewSize.width, previewSize.height)
            setPictureSize(previewSize.width, previewSize.height)
            calcScale(textureView, rotation % 180 == 90, previewSize)
        }
        camera.startPreview()
    }

    private fun calcScale(view: View, isPortrait: Boolean, previewSize: Camera.Size) {
        val width = mWidth
        val height = mHeight
        val ratioSurface: Float =
            if (width > height) width.toFloat() / height else height.toFloat() / width
        val ratioPreview: Float = previewSize.width.toFloat() / previewSize.height

        var scaledHeight = 0
        var scaledWidth = 0
        var scaleX = 1f
        var scaleY = 1f

        if (isPortrait && ratioPreview > ratioSurface) {
            scaledWidth = width
            scaledHeight = (previewSize.width.toFloat() / previewSize.height * width).toInt()
            scaleX = 1f
            scaleY = scaledHeight.toFloat() / height
        } else if (isPortrait && ratioPreview < ratioSurface) {
            scaledWidth = (height / (previewSize.width.toFloat() / previewSize.height)).toInt()
            scaledHeight = height
            scaleX = scaledWidth.toFloat() / width
            scaleY = 1f
        } else if (!isPortrait && ratioPreview < ratioSurface) {
            scaledWidth = width
            scaledHeight = (width / (previewSize.width.toFloat() / previewSize.height)).toInt()
            scaleX = 1f
            scaleY = scaledHeight.toFloat() / height
        } else if (!isPortrait && ratioPreview > ratioSurface) {
            scaledWidth = (previewSize.width.toFloat() / previewSize.height * width).toInt()
            scaledHeight = height
            scaleX = scaledWidth.toFloat() / width
            scaleY = 1f
        }
        Log.i(TAG, "scale <${scaleX},${scaleY}>")
        view.scaleX = scaleX
        view.scaleY = scaleY
    }

    private fun releaseCamera() {
        mCamera?.run {
            stopPreview()
            release()
        }
        mCamera = null
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseCamera()
    }

    private fun getCameraDisplayOrientation(info: Camera.CameraInfo): Int {
        val rotation = this.windowManager.defaultDisplay.rotation
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }

        var result: Int
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360
            result = (360 - result) % 360  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360
        }
        return result
    }
}
