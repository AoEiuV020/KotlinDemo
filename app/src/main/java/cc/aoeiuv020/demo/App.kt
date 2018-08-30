package cc.aoeiuv020.demo

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.net.NetworkInterface
import java.util.*


class App : Application(), AnkoLogger {

    override fun onCreate() {
        super.onCreate()

        initUmeng()
    }

    private fun initUmeng() {
        // 初始化友盟，appKey和channel在manifest中配置，
        UMConfigure.init(applicationContext, UMConfigure.DEVICE_TYPE_PHONE, null)
        // 默认就捕获异常，
        MobclickAgent.setCatchUncaughtExceptions(true)
        // 启用日志，限制debug版并且tag UM的日志级别为DEBUG,
        UMConfigure.setLogEnabled(BuildConfig.DEBUG && Log.isLoggable("UM", Log.DEBUG))
        (BuildConfig.DEBUG && Log.isLoggable("UM", Log.DEBUG))
        UMConfigure.setLogEnabled(true)
        info {
            "device info: " + getDeviceInfo(applicationContext)
        }
        /**
         * 绑定所有activity的生命周期，调用友盟统计时长，
         */
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
                info { "onActivityPaused: ${activity?.javaClass?.simpleName}" }
                MobclickAgent.onPause(activity)
            }

            override fun onActivityResumed(activity: Activity?) {
                info { "onActivityResumed: ${activity?.javaClass?.simpleName}" }
                MobclickAgent.onResume(activity)
            }

            override fun onActivityStarted(activity: Activity?) {
                info { "onActivityStarted: ${activity?.javaClass?.simpleName}" }
            }

            override fun onActivityDestroyed(activity: Activity?) {
                info { "onActivityDestroyed: ${activity?.javaClass?.simpleName}" }
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
                info { "onActivitySaveInstanceState: ${activity?.javaClass?.simpleName}" }
            }

            override fun onActivityStopped(activity: Activity?) {
                info { "onActivityStopped: ${activity?.javaClass?.simpleName}" }
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                info { "onActivityCreated: ${activity?.javaClass?.simpleName}" }
            }
        })
    }

    @SuppressLint("HardwareIds")
    private fun getDeviceInfo(context: Context): String? {
        try {
            val json = org.json.JSONObject()
            val tm = context
                    .getSystemService(Context.TELEPHONY_SERVICE) as android.telephony.TelephonyManager
            var deviceId: String? = null
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                @Suppress("DEPRECATION")
                deviceId = tm.deviceId
            }
            val mac = getMac(context)

            json.put("mac", mac)
            if (TextUtils.isEmpty(deviceId)) {
                deviceId = mac
            }
            if (TextUtils.isEmpty(deviceId)) {
                deviceId = android.provider.Settings.Secure.getString(context.contentResolver,
                        android.provider.Settings.Secure.ANDROID_ID)
            }
            json.put("device_id", deviceId)
            return json.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    private fun getMac(context: Context?): String? {
        var mac: String? = ""
        if (context == null) {
            return mac
        }
        if (Build.VERSION.SDK_INT < 23) {
            mac = getMacBySystemInterface(context)
        } else {
            mac = getMacByJavaAPI()
            if (TextUtils.isEmpty(mac)) {
                mac = getMacBySystemInterface(context)
            }
        }
        return mac

    }

    @TargetApi(9)
    private fun getMacByJavaAPI(): String? {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val netInterface = interfaces.nextElement()
                if ("wlan0" == netInterface.name || "eth0" == netInterface.name) {
                    val addr = netInterface.hardwareAddress
                    if (addr == null || addr.isEmpty()) {
                        return null
                    }
                    val buf = StringBuilder()
                    for (b in addr) {
                        buf.append(String.format("%02X:", b))
                    }
                    if (buf.isNotEmpty()) {
                        buf.deleteCharAt(buf.length - 1)
                    }
                    return buf.toString().toLowerCase(Locale.getDefault())
                }
            }
        } catch (e: Throwable) {
        }

        return null
    }

    @SuppressLint("HardwareIds")
    private fun getMacBySystemInterface(context: Context?): String {
        if (context == null) {
            return ""
        }
        return try {
            val wifi = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            if (checkPermission(context, Manifest.permission.ACCESS_WIFI_STATE)) {
                val info = wifi.connectionInfo
                info.macAddress
            } else {
                ""
            }
        } catch (e: Throwable) {
            ""
        }

    }

    private fun checkPermission(context: Context?, permission: String): Boolean {
        var result = false
        if (context == null) {
            return result
        }
        if (Build.VERSION.SDK_INT >= 23) {
            result = try {
                val clazz = Class.forName("android.content.Context")
                val method = clazz.getMethod("checkSelfPermission", String::class.java)
                val rest = method.invoke(context, permission) as Int
                rest == PackageManager.PERMISSION_GRANTED
            } catch (e: Throwable) {
                false
            }

        } else {
            val pm = context.packageManager
            if (pm.checkPermission(permission, context.packageName) == PackageManager.PERMISSION_GRANTED) {
                result = true
            }
        }
        return result
    }

}
