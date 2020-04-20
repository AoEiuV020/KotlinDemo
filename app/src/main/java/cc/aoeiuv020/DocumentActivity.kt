package cc.aoeiuv020

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.documentfile.provider.DocumentFile
import cc.aoeiuv020.anull.notNull
import kotlinx.android.synthetic.main.activity_document.*
import kotlinx.android.synthetic.main.activity_storage.btnRead
import kotlinx.android.synthetic.main.activity_storage.btnWrite
import kotlinx.android.synthetic.main.activity_storage.etPath
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.*

class DocumentActivity : AppCompatActivity() {
    companion object {
        @Suppress("unused")
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun start(ctx: Context) {
            ctx.startActivity<DocumentActivity>()
        }
    }

    private var treeUri: Uri? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document)

        ActivityCompat.requestPermissions(this, packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS).requestedPermissions, 1)

        btnRequest.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            startActivityForResult(intent, 1)
        }

        btnRead.setOnClickListener {
            val s = contentResolver.openInputStream(getUri()).notNull().reader().use { input ->
                input.readText()
            }
            toast(s)
        }

        btnWrite.setOnClickListener {
            contentResolver.openOutputStream(getUri()).notNull().writer().use { output ->
                output.write("android q document ${Date()}")
            }
        }
    }

    private fun getUri(): Uri {
        val t = DocumentFile.fromTreeUri(this, treeUri.notNull("treeUri")).notNull("fromTreeUri")
        val n = etPath.text.toString()
        val f = t.findFile(n) ?: t.createFile("text/plain", n).notNull("createFile")
        return f.uri
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            1 -> treeUri = data?.data
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
