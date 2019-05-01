package cc.aoeiuv020

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private var oResources: SkinResource? = null

    override fun getResources(): Resources {
        return oResources ?: SkinResource(
                this,
                super.getResources()
        ).also { oResources = it }
    }

    class SkinResource(
            ctx: Context,
            resources: Resources
    ) : Resources(
            ctx.assets,
            resources.displayMetrics,
            resources.configuration
    ) {

        override fun getColor(id: Int, theme: Theme?): Int {
            return when (id) {
                R.color.colorPrimaryDark -> 0xff00ff00.toInt()
                R.color.colorPrimary -> 0xff00ff00.toInt()
                R.color.colorAccent -> 0xff00ff00.toInt()
                else -> super.getColor(id, theme)
            }
        }

        override fun getColor(id: Int): Int {
            return getColor(id, null)
        }
    }
}
