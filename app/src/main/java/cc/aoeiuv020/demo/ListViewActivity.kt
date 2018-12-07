package cc.aoeiuv020.demo

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_list_view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity

class ListViewActivity : AppCompatActivity(), AnkoLogger {
    companion object {
        fun start(ctx: Context) {
            ctx.startActivity<ListViewActivity>()
        }
    }

    private val data: MutableList<Item> = MutableList(88) {
        Item(it.toString())
    }
    private var isBottom = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view)

        listView.adapter = MyAdapter()

        listView.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                info {
                    "onScroll <$firstVisibleItem, $visibleItemCount, $totalItemCount>"
                }
                isBottom = firstVisibleItem + visibleItemCount >= totalItemCount
            }

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                info {
                    "onScrollStateChanged $scrollState"
                }
            }
        })
        listView.setOnItemClickListener { parent, _, _, _ ->
            data.add(data.size, Item(data.size.toString()))
            (parent.adapter as MyAdapter).notifyDataSetChanged()
            if (isBottom) {
                parent.setSelection(parent.adapter.count)
            }
        }
    }

    inner class MyAdapter : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return TextView(parent.context).apply {
                text = getItem(position).text
            }
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

    data class Item(
            val text: String
    )
}
