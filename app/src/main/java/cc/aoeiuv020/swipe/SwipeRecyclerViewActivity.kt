package cc.aoeiuv020.swipe

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.aoeiuv020.R
import kotlinx.android.synthetic.main.activity_swipe_recycler_view.*
import kotlinx.android.synthetic.main.item_swipe_recycler_view.view.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.startActivity

class SwipeRecyclerViewActivity : AppCompatActivity() {
    companion object {
        fun start(ctx: Context) {
            ctx.startActivity<SwipeRecyclerViewActivity>()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe_recycler_view)

        recyclerView.run {
            layoutManager = LinearLayoutManager(ctx)
            adapter = MyAdapter()
        }
    }

    class Item(
            val number: Int
    )

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNumber: TextView = itemView.tvNumber
    }

    class MyAdapter : RecyclerView.Adapter<MyViewHolder>() {
        private var data: MutableList<Item> = MutableList(88) {
            Item(it)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_swipe_recycler_view, parent, false)
            return MyViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val item = data[position]
            holder.tvNumber.text = item.number.toString()
        }
    }
}
