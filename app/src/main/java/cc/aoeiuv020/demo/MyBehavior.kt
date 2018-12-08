package cc.aoeiuv020.demo

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.util.Log
import android.view.View

import java.lang.ref.WeakReference

class MyBehavior(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<View>(context, attrs) {

    private var dependentView: WeakReference<View>? = null

    private fun getDependentView(): View {
        return dependentView!!.get()!!
    }

    override fun layoutDependsOn(parent: CoordinatorLayout?, child: View?, dependency: View?): Boolean {
        if (dependency != null && dependency.id == R.id.move) {
            dependentView = WeakReference(dependency)
            return true
        }
        return super.layoutDependsOn(parent, child, dependency)
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout?, child: View?, dependency: View?): Boolean {
        child!!.translationY = dependency!!.height + dependency.translationY
        return true
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: View, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {

        return axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)

        if (dy < 0) {
            return
        }
        val dependentView = getDependentView()
        val newTranslateY = dependentView.translationY - dy
        val minHeaderTranslate = (-dependentView.height).toFloat()
        Log.i("onLayoutChild", "onNestedPreScroll dy=" + dy + "TranslationY='" + dependentView.translationY)
        if (newTranslateY >= minHeaderTranslate) {
            dependentView.translationY = newTranslateY
            consumed[1] = dy
        } else {
            if (dependentView.translationY >= -minHeaderTranslate) {
                consumed[1] = (dependentView.translationY - minHeaderTranslate).toInt()
            }
            dependentView.translationY = minHeaderTranslate

        }
    }


    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {

        if (dyUnconsumed > 0) {
            return
        }
        val dependentView = getDependentView()
        val currentTranslationY = dependentView.translationY
        val newTranslateY = currentTranslationY - dyUnconsumed
        val maxHeaderTranslate = 0f
        Log.i("onLayoutChild", "onNestedScroll dyUnconsumed=" + dyUnconsumed + "currentTranslationY=" + currentTranslationY)
        if (newTranslateY <= maxHeaderTranslate) {
            dependentView.translationY = newTranslateY
        } else {
            dependentView.translationY = maxHeaderTranslate
        }


        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
    }
}
