package cc.aoeiuv020

import android.app.Activity
import android.os.Bundle
import androidx.multidex.MultiDexApplication
import cc.aoeiuv020.anull.notNull

class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {

            }

            override fun onActivityResumed(activity: Activity?) {

            }

            override fun onActivityStarted(activity: Activity?) {

            }

            override fun onActivityDestroyed(activity: Activity?) {

            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

            }

            override fun onActivityStopped(activity: Activity?) {

            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                activity.notNull().setTheme(R.style.Black)
            }
        })
    }
}
