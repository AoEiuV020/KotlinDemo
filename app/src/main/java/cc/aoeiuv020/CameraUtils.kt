@file:Suppress("DEPRECATION")

package cc.aoeiuv020

import android.graphics.Bitmap
import android.graphics.Matrix
import android.hardware.Camera
import android.hardware.Camera.CameraInfo

object CameraUtils {
    fun restoreRotatedImage(degrees: Int, bitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees.toFloat())

        val width = bitmap.width
        val height = bitmap.height
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
    }

    fun getCameraInfo(cameraId: Int): CameraInfo {
        val info = CameraInfo()
        Camera.getCameraInfo(cameraId, info)
        return info
    }
}