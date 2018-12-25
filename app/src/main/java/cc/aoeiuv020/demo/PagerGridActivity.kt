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
import org.jetbrains.anko.toast
import kotlin.random.Random

class PagerGridActivity : AppCompatActivity(), PagerGridAdapterFactory<Item>, OnItemClickListener<Item> {
    companion object {
        fun start(ctx: Context) {
            ctx.startActivity<PagerGridActivity>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pager_grid)
        vpContent.adapter = GridPagerAdapter(loadData(), 4, 2, this, this)
    }

    private fun loadData(): List<Item> = List(Random.nextInt(100, 200)) {
        Item(it.toString(), Runnable {
            toast("onItemClick: $it")
        })
    }

    override fun onItemClick(item: Item) {
        item.runnable.run()
    }

    override fun createPagerGridAdapter(data: List<Item>): ListAdapter {
        return PagerGridAdapter(data, vpContent)
    }

    class GridPagerAdapter<T>(
            private val data: List<T>,
            private val columnCount: Int,
            rowCount: Int,
            private val factory: PagerGridAdapterFactory<T>,
            private val listener: OnItemClickListener<T>
    ) : PagerAdapter() {
        private val pageSize = rowCount * columnCount
        private fun getPageData(page: Int): List<T> {
            return data.subList(page * pageSize, minOf(((page + 1) * pageSize), data.size))
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val grid = GridView(container.context)
            grid.numColumns = columnCount
            grid.adapter = factory.createPagerGridAdapter(getPageData(position))
            grid.setOnItemClickListener { parent, _, itemPosition, _ ->
                @Suppress("UNCHECKED_CAST")
                listener.onItemClick(parent.adapter.getItem(itemPosition) as T)
            }
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
            private val data: List<Item>,
            private val viewPager: View
    ) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: TextView(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
                viewPager.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                    override fun onLayoutChange(v: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                        layoutParams = layoutParams.apply {
                            height = v.height / 2
                        }
                        v.removeOnLayoutChangeListener(this)
                    }
                })
                gravity = Gravity.CENTER
                background = ColorDrawable(0xffff0000L.toInt())
            }
            val item = getItem(position)
            val tv = view as TextView
            tv.text = item.text
            return view
        }

        override fun getItem(position: Int): Item {
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

data class Item(
        val text: String,
        val runnable: Runnable
)

interface PagerGridAdapterFactory<T> {
    fun createPagerGridAdapter(data: List<T>): ListAdapter
}

interface OnItemClickListener<T> {
    fun onItemClick(item: T)
}
