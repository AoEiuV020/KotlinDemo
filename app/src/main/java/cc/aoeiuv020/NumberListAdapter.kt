package cc.aoeiuv020

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NumberListAdapter : RecyclerView.Adapter<NumberListAdapter.ViewHolder>() {
    private val data: List<Item> = List(88) { Item(it) }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TextView(parent.context))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        (holder.itemView as TextView).text = item.number.toString()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class Item(val number: Int)
}
