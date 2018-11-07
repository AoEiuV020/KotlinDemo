package cc.aoeiuv020.demo

import android.content.Context
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_grid_view.*
import kotlinx.android.synthetic.main.item_square_action.view.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.dip
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import kotlin.math.max


class GridViewActivity : AppCompatActivity() {
    companion object {
        fun start(ctx: Context) {
            ctx.startActivity<GridViewActivity>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid_view)

        gvAction.adapter = SquareActionAdapter(getData())
        gvAction.numColumns = 2
    }

    private fun getData(): List<Item> {
        return List(12) {
            Item(R.string.app_name, R.drawable.item_square, toToast())
        }
    }

    private fun toToast() = Runnable {
        toast("点击")
    }

    private inner class SquareActionAdapter(
            private val data: List<Item>
    ) : BaseAdapter() {
        private fun resetLayoutSize(
                itemView: View,
                parent: ViewGroup,
                llRoot: View
        ) {
            itemView.run {
                layoutParams = layoutParams.apply {
                    height = max(
                            llRoot.height + ctx.dip(30),
                            parent.height / 3
                    )
                }
            }
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val vh = (convertView?.tag as? ViewHolder)
                    ?: onCreateViewHolder(parent)
            vh.itemView.tag = vh

            // 不确定效果，布局改变时的回调不熟悉，
            resetLayoutSize(vh.itemView, parent, vh.llRoot)
            parent.addOnLayoutChangeListener { _, _, top, _, bottom, _, _, _, _ ->
                resetLayoutSize(vh.itemView, parent, vh.llRoot)
            }
            vh.llRoot.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                resetLayoutSize(vh.itemView, parent, vh.llRoot)
            }

            onBindViewHolder(vh, position)

            return vh.itemView
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

        fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
            val itemView = layoutInflater.inflate(R.layout.item_square_action, parent, false)
            return ViewHolder(itemView)
        }

        fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = data[position]
            holder.itemView.setOnClickListener { item.onClickCallback.run() }
            holder.ivActionImage.setImageResource(item.imageRes)
            holder.tvActionName.setText(item.textRes)
        }
    }

    private class ViewHolder(
            val itemView: View
    ) {
        val llRoot: LinearLayout = itemView.llRoot
        val tvActionName: TextView = itemView.tvActionName
        val ivActionImage: ImageView = itemView.ivActionImage
    }

    private class Item(
            @param:StringRes val textRes: Int,
            @param:DrawableRes val imageRes: Int,
            val onClickCallback: Runnable
    )
}

