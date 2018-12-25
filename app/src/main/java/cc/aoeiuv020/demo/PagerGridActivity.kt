package cc.aoeiuv020.demo

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ListAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_pager_grid.*
import org.jetbrains.anko.startActivity
import kotlin.random.Random

class PagerGridActivity : AppCompatActivity(), PagerGridAdapterFactory<Int> {
    companion object {
        fun start(ctx: Context) {
            ctx.startActivity<PagerGridActivity>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pager_grid)
        vpContent.adapter = GridPagerAdapter(loadData(), 4, 2, this)
    }

    private fun loadData(): List<Int> = List(Random.nextInt(10, 20)) {
        it
    }

    override fun createPagerGridAdapter(data: List<Int>): ListAdapter {
        return PagerGridAdapter(data, vpContent)
    }

    class GridPagerAdapter<T>(
            private val data: List<T>,
            private val columnCount: Int,
            rowCount: Int,
            private val factory: PagerGridAdapterFactory<T>
    ) : PagerAdapter() {
        private val pageSize = rowCount * columnCount
        private fun getPageData(page: Int): List<T> {
            return data.subList(page * pageSize, minOf(((page + 1) * pageSize), data.size))
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val grid = GridView(container.context)
            grid.numColumns = columnCount
            grid.adapter = factory.createPagerGridAdapter(getPageData(position))
            container.addView(grid, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            return grid
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun getCount(): Int {
            return (data.size + (pageSize - 1)) / pageSize
        }
    }

    class PagerGridAdapter(
            private val data: List<Int>,
            private val viewPager: View
    ) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: TextView(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
                viewPager.addOnLayoutChangeListener { v, _, _, _, _, _, _, _, _ ->
                    layoutParams = layoutParams.apply {
                        height = v.height / 2
                    }
                }
                gravity = Gravity.CENTER
                background = ColorDrawable(0xffff0000L.toInt())
            }
            val item = getItem(position)
            val tv = view as TextView
            tv.text = item.toString()
            return view
        }

        override fun getItem(position: Int): Int {
            return data[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return data.size
        }
    }
}

interface PagerGridAdapterFactory<T> {
    fun createPagerGridAdapter(data: List<T>): ListAdapter
}
