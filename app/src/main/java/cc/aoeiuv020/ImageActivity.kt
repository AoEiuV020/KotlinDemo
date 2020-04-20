package cc.aoeiuv020

import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import cc.aoeiuv020.anull.notNull
import kotlinx.android.synthetic.main.activity_image.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread
import java.io.OutputStream
import java.net.URL


class ImageActivity : AppCompatActivity() {
    companion object {
        @Suppress("unused")
        fun start(ctx: Context) {
            ctx.startActivity<ImageActivity>()
        }
    }

    private val imageName = "imageAndroidQTest.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        ActivityCompat.requestPermissions(this, packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS).requestedPermissions, 1)

        btnDownload.setOnClickListener {
            doAsync {
                URL(getUrl()).openStream().use { input ->
                    openFileOutput(imageName, Context.MODE_PRIVATE).use { output ->
                        input.copyTo(output)
                    }
                }
                uiThread {
                    ivImage.setImageURI(Uri.fromFile(getFileStreamPath(imageName)))
                }
            }
        }

        btnSave.setOnClickListener {
            addPictureToAlbum(this, imageName) { output ->
                openFileInput(imageName).use { input ->
                    input.copyTo(output)
                }
            }
        }
    }

    private fun getUrl(): String {
        return etUrl.text.toString()
    }

    @Suppress("SameParameterValue")
    private fun addPictureToAlbum(context: Context, fileName: String, writer: (OutputStream) -> Unit) {
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, fileName)
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues).notNull()
        context.contentResolver.openOutputStream(uri).notNull().use(writer)
    }
}