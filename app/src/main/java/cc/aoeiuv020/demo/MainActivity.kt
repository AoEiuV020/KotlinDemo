package cc.aoeiuv020.demo

import android.Manifest.permission.READ_PHONE_STATE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import com.qq.e.ads.banner.ADSize
import com.qq.e.ads.banner.BannerADListener
import com.qq.e.ads.banner.BannerView
import com.qq.e.comm.util.AdError
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.findOptional
import org.jetbrains.anko.info


class MainActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) == PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
            showBanner()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(READ_PHONE_STATE, WRITE_EXTERNAL_STORAGE), 0)
        }
    }

    private fun showBanner() {
        // 官方demo里的id, 自己的审核中，报错，100135 广告位被联盟后台封禁或违规暂停
        val banner = BannerView(this, ADSize.BANNER, "1101152570", "9079537218417626401")
        banner.setADListener(object : BannerADListener {
            override fun onADCloseOverlay() {
                info { "onADCloseOverlay" }
            }

            override fun onADExposure() {
                info { "onADExposure" }
            }

            override fun onADClosed() {
                info { "onADClosed" }
            }

            override fun onADLeftApplication() {
                info { "onADLeftApplication" }
            }

            override fun onADOpenOverlay() {
                info { "onADOpenOverlay" }
            }

            override fun onNoAD(p0: AdError?) {
                info { "onNoAD: ${p0?.errorCode}, ${p0?.errorMsg}" }
            }

            override fun onADClicked() {
                info { "onADClicked" }
            }

            override fun onADReceiv() {
                info { "onADReceiv" }
            }
        })

        banner.id = R.id.adViewBanner
        lAdContainer.addView(banner)
        banner.loadAD()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 0 && grantResults.all { it == PERMISSION_GRANTED }) {
            showBanner()
        }
    }

    override fun onStart() {
        super.onStart()
        findOptional<BannerView>(R.id.adViewBanner)?.setRefresh(30)
    }

    override fun onPause() {
        findOptional<BannerView>(R.id.adViewBanner)?.setRefresh(0)
        super.onPause()
    }
}
