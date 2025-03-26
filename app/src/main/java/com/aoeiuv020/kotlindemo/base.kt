package com.aoeiuv020.kotlindemo

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import me.imid.swipebacklayout.lib.SwipeBackLayout
import me.imid.swipebacklayout.lib.Utils
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper

abstract class SwipeBackActivity : Activity(), SwipeBackActivityBase {
    // 侧滑这个影响activity透明，所以图片相关几个页面禁用侧滑，连初始化都不可以，
    protected var disableSwipeBack: Boolean = false
    private var mHelper: SwipeBackActivityHelper? = null

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) // 竖屏
        if (!disableSwipeBack) {
            mHelper = SwipeBackActivityHelper(this)
            mHelper!!.onActivityCreate()
        }
    }

    protected override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (!disableSwipeBack) {
            mHelper!!.onPostCreate()
        }
    }

    override fun setRequestedOrientation(requestedOrientation: Int) {
        try {
            super.setRequestedOrientation(requestedOrientation)
        } catch (e: Exception) {
            // 不要崩溃，不能设置就不设置，
            // 安卓8不允许透明页面设置屏幕方向，侧滑又给所有页面设置了透明，因此所有页面在侧滑初始化完成后都不能设置方向，
            // 项目里原本就是初始化时固定竖屏的，该设置移到侧滑初始化前，
            Log.d("SwipeBack", "忽略安卓8的屏幕旋转")
        }
    }

    override fun <T : View?> findViewById(id: Int): T? {
        return realFindViewById(id) as T?
    }

    fun realFindViewById(id: Int): View? {
        val v: View = super.findViewById(id)
        if (v == null && mHelper != null) return mHelper!!.findViewById(id)
        return v
    }

    override fun finish() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view: View? = getCurrentFocus()
        if (view == null) {
            view = getWindow().getDecorView()
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        super.finish()
    }

    override fun getSwipeBackLayout(): SwipeBackLayout? {
        if (disableSwipeBack) {
            return null
        }
        return mHelper!!.swipeBackLayout
    }

    override fun setSwipeBackEnable(enable: Boolean) {
        if (disableSwipeBack) {
            return
        }
        swipeBackLayout!!.setEnableGesture(enable)
    }

    override fun scrollToFinishActivity() {
        if (disableSwipeBack) {
            return
        }
        Utils.convertActivityToTranslucent(this)
        swipeBackLayout!!.scrollToFinishActivity()
    }
}
