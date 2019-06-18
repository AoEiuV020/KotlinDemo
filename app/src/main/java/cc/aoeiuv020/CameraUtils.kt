@file:Suppress("DEPRECATION")

package cc.aoeiuv020

import android.graphics.Bitmap
import android.graphics.Matrix
import android.hardware.Camera
import android.hardware.Camera.CameraInfo
import cc.aoeiuv020.anull.notNull

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

    //获取摄像头支持的最接近的尺寸
    fun getOptimalPreviewSize(sizes: List<Camera.Size>, w: Int, h: Int): Camera.Size {
        val ASPECT_TOLERANCE = 0.2
        val targetRatio = w.toDouble() / h

        var optimalSize: Camera.Size? = null
        var minDiff = java.lang.Double.MAX_VALUE

        // Try to find an size match aspect ratio and size
        for (size in sizes) {
            val ratio = size.width.toDouble() / size.height
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue
            if (Math.abs(size.height - h) < minDiff) {
                optimalSize = size
                minDiff = Math.abs(size.height - h).toDouble()
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = java.lang.Double.MAX_VALUE
            for (size in sizes) {
                if (Math.abs(size.height - h) < minDiff) {
                    optimalSize = size
                    minDiff = Math.abs(size.height - h).toDouble()
                }
            }
        }
        return optimalSize.notNull()
    }
}