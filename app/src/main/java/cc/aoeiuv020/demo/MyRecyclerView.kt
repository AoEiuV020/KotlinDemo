package cc.aoeiuv020.demo

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

class MyRecyclerView : RecyclerView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(widthSpec, minOf(heightSpec, context.resources.displayMetrics.heightPixels))
    }
}