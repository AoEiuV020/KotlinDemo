package cc.aoeiuv020.demo

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_grid_recycler_view.*
import kotlinx.android.synthetic.main.item_square_action.view.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.*

class GridRecyclerViewActivity : AppCompatActivity() {
    companion object {
        fun start(ctx: Context) {
            ctx.startActivity<GridRecyclerViewActivity>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid_recycler_view)

        recyclerView.layoutManager = GridLayoutManager(ctx, 2)
        recyclerView.adapter = SquareActionAdapter(getData())
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                outRect.top = 30
                outRect.bottom = 30
            }
        })
    }

    private fun getData(): List<Item> {
        return Arrays.asList(
                Item(R.string.app_name, R.drawable.item_square, toToast()),
                Item(R.string.app_name, R.drawable.item_square, toToast()),
                Item(R.string.app_name, R.drawable.item_square, toToast()),
                Item(R.string.app_name, R.drawable.item_square, toToast()),
                Item(R.string.app_name, R.drawable.item_square, toToast()),
                Item(R.string.app_name, R.drawable.item_square, toToast())
        )
    }

    private fun toToast() = Runnable {
        toast("点击")
    }

    private inner class SquareActionAdapter(
            private val data: List<Item>
    ) : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = layoutInflater.inflate(R.layout.item_square_action, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = data[position]
            holder.itemView.setOnClickListener { item.onClickCallback.run() }
            holder.ivActionImage.setImageResource(item.imageRes)
            holder.tvActionName.setText(item.textRes)
        }

        override fun getItemCount(): Int {
            return data.size
        }
    }

    private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvActionName: TextView = itemView.tvActionName
        val ivActionImage: ImageView = itemView.ivActionImage
    }

    private class Item(
            @param:StringRes val textRes: Int,
            @param:DrawableRes val imageRes: Int,
            val onClickCallback: Runnable
    )

}
