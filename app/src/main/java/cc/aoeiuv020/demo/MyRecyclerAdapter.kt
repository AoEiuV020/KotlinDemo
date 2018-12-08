package cc.aoeiuv020.demo

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MyRecyclerAdapter : RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder>(), AnkoLogger {
    private val data: MutableList<Item> = MutableList(222) {
        Item(it.toString())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = TextView(parent.context)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        info { "bind $position" }
        holder.bind(data[position])
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