package cc.aoeiuv020.demo

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class MyGridAdapter : BaseAdapter() {
    private val data: MutableList<Item> = MutableList(888) {
        Item(it.toString())
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: TextView(parent.context).apply {
            tag = MyViewHolder(this)
        }
        val vh = view.tag as MyViewHolder
        vh.bind(getItem(position))
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

    class MyViewHolder(
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView as TextView

        fun bind(item: Item) {
            textView.text = item.text
        }
    }

    data class Item(
            val text: String
    )
}