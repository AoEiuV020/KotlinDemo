package cc.aoeiuv020.demo.share.demo

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import java.io.File


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 没权限的话这里正常，传出去的uri读不出图片，
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)

        btnSendText.setOnClickListener {
            val content = etContent.text.toString()
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, "分享纯文字")
            intent.putExtra(Intent.EXTRA_TEXT, content)
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                toast("没有对应Activity,")
            }
        }

        btnSendImage.setOnClickListener {
            val file = File(etFile.text.toString())
            if (!file.exists() || !file.isFile) {
                toast("不是文件，")
                return@setOnClickListener
            }
            val uri = FileProvider.getUriForFile(this@MainActivity, BuildConfig.APPLICATION_ID + ".fileProvider", file)
            val content = etContent.text.toString()
            val intent = Intent(Intent.ACTION_SEND)
            val type = "image/*"
            intent.setDataAndType(uri, type)
            intent.putExtra(Intent.EXTRA_SUBJECT, "分享图片")
            intent.putExtra(Intent.EXTRA_TEXT, content)
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                toast("没有对应Activity,")
            }
        }

        btnSendVideo.setOnClickListener {
            val file = File(etFile.text.toString())
            if (!file.exists() || !file.isFile) {
                toast("不是文件，")
                return@setOnClickListener
            }
            val uri = FileProvider.getUriForFile(this@MainActivity, BuildConfig.APPLICATION_ID + ".fileProvider", file)
            val content = etContent.text.toString()
            val intent = Intent(Intent.ACTION_SEND)
            val type = "video/*"
            intent.setDataAndType(uri, type)
            intent.putExtra(Intent.EXTRA_SUBJECT, "分享视频")
            intent.putExtra(Intent.EXTRA_TEXT, content)
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                toast("没有对应Activity,")
            }
        }
    }
}
