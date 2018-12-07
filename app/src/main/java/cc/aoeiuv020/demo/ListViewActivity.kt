package cc.aoeiuv020.demo

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_list_view.*
import org.jetbrains.anko.startActivity

class ListViewActivity : AppCompatActivity() {
    private val data: List<Item> = List(88) {
        Item(it.toString())
    }

    companion object {
        fun start(ctx: Context) {
            ctx.startActivity<ListViewActivity>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view)

        listView.adapter = MyAdapter()
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
