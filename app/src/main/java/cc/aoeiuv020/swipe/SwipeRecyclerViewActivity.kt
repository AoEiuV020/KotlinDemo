package cc.aoeiuv020.swipe

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.aoeiuv020.R
import com.yanzhenjie.recyclerview.*
import kotlinx.android.synthetic.main.activity_swipe_recycler_view.*
import kotlinx.android.synthetic.main.item_swipe_recycler_view.view.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.dip
import org.jetbrains.anko.startActivity
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class SwipeRecyclerViewActivity : AppCompatActivity() {
    companion object {
        fun start(ctx: Context) {
            ctx.startActivity<SwipeRecyclerViewActivity>()
        }

    }

    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */
    private val swipeMenuCreator = object : SwipeMenuCreator {
        override fun onCreateMenu(swipeLeftMenu: SwipeMenu, swipeRightMenu: SwipeMenu, position: Int) {
            val width = dip(70)

            // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
            // 2. 指定具体的高，比如80;
            // 3. WRAP_CONTENT，自身高度，不推荐;
            val height = ViewGroup.LayoutParams.MATCH_PARENT

            // 添加左侧的，如果不添加，则左侧不会出现菜单。
            run {
                val addItem = SwipeMenuItem(ctx).setBackgroundColor(0xff00ff00.toInt())
                        .setImage(android.R.drawable.ic_menu_call)
                        .setWidth(width)
                        .setHeight(height)
                swipeLeftMenu.addMenuItem(addItem) // 添加菜单到左侧。

                val closeItem = SwipeMenuItem(ctx).setBackgroundColor(0xffff0000.toInt())
                        .setWidth(width)
                        .setHeight(height)
                swipeLeftMenu.addMenuItem(closeItem) // 添加菜单到左侧。
            }

            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            run {
                val deleteItem = SwipeMenuItem(ctx).setBackgroundColor(0xffff0000.toInt())
                        .setImage(android.R.drawable.ic_delete)
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height)
                swipeRightMenu.addMenuItem(deleteItem)// 添加菜单到右侧。

                val addItem = SwipeMenuItem(ctx).setBackgroundColor(0xff00ff00.toInt())
                        .setImage(android.R.drawable.ic_input_add)
                        .setText("添加")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height)
                swipeRightMenu.addMenuItem(addItem) // 添加菜单到右侧。
            }
        }
    }

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private val mMenuItemClickListener = OnItemMenuClickListener { menuBridge, position ->
        menuBridge.closeMenu()

        val direction = menuBridge.direction // 左侧还是右侧菜单。
        val menuPosition = menuBridge.position // 菜单在RecyclerView的Item中的Position。

        if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
            Toast.makeText(ctx, "list第$position; 右侧菜单第$menuPosition", Toast.LENGTH_SHORT)
                    .show()
            if (menuPosition == 0) {
                // 删除item,
                mAdapter.remove(position)
            } else {
                refreshLayout.autoRefresh()
            }
        } else if (direction == SwipeRecyclerView.LEFT_DIRECTION) {
            Toast.makeText(ctx, "list第$position; 左侧菜单第$menuPosition", Toast.LENGTH_SHORT)
                    .show()
        }
    }

    private lateinit var mAdapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe_recycler_view)

        recyclerView.setSwipeMenuCreator(swipeMenuCreator)
        recyclerView.setOnItemMenuClickListener(mMenuItemClickListener)
        recyclerView.run {
            layoutManager = LinearLayoutManager(ctx)
            adapter = MyAdapter().also {
                mAdapter = it
            }
        }

        refreshLayout.setOnRefreshListener {
            refreshLayout.postDelayed({
                repeat(4) {
                    mAdapter.add(0, Item(mAdapter.minValue() - 1))
                }
                it.finishRefresh()
            }, TimeUnit.SECONDS.toMillis(1))
        }

        refreshLayout.setOnLoadMoreListener {
            refreshLayout.postDelayed({
                repeat(4) {
                    mAdapter.add(mAdapter.itemCount, Item(mAdapter.maxValue() + 1))
                }
                it.finishLoadMore()
            }, TimeUnit.SECONDS.toMillis(1))
        }
    }

    class Item(
            val number: Int
    )

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNumber: TextView = itemView.tvNumber
    }

    class MyAdapter : RecyclerView.Adapter<MyViewHolder>() {
        private var data: MutableList<Item> = MutableList(33) {
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
            holder.tvNumber.setBackgroundColor(Random.nextInt())
            holder.itemView.setOnClickListener {
                val removeAt = data.removeAt(position)
                data.add(0, removeAt)
                notifyItemMoved(position, 0)
            }
        }

        fun minValue(): Int {
            return data.first().number
        }

        fun maxValue(): Int {
            return data.last().number
        }

        fun remove(position: Int) {
            data.removeAt(position)
            notifyItemRemoved(position)
        }

        fun add(index: Int, item: Item) {
            data.add(index, item)
            notifyItemInserted(index)
        }
    }
}
