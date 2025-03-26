package com.aoeiuv020.kotlindemo

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import me.imid.swipebacklayout.lib.SwipeBackLayout
import me.imid.swipebacklayout.lib.Utils
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper

class ListActivity : Activity(), SwipeBackActivityBase {
    private lateinit var mHelper: SwipeBackActivityHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        mHelper = SwipeBackActivityHelper(this)
        mHelper.onActivityCreate()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)!!
        recyclerView.layoutManager = LinearLayoutManager(this)
        val numbers = (1..100).toList()
        recyclerView.adapter = NumberAdapter(numbers)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
            mHelper.onPostCreate()
    }

    override fun <T : View?> findViewById(id: Int): T? {
        return realFindViewById(id) as T?
    }

    fun realFindViewById(id: Int): View? {
        val v: View = super.findViewById(id)
        if (v == null && mHelper != null) return mHelper.findViewById(id)
        return v
    }

    override fun getSwipeBackLayout(): SwipeBackLayout {
        return mHelper.getSwipeBackLayout()
    }

    override fun setSwipeBackEnable(enable: Boolean) {
        getSwipeBackLayout().setEnableGesture(enable)
    }

    override fun scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this)
        getSwipeBackLayout().scrollToFinishActivity()
    }
}

class NumberAdapter(private val numbers: List<Int>) : RecyclerView.Adapter<NumberViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_number, parent, false)
        return NumberViewHolder(view).apply {
            itemView.setOnClickListener {
                val number = numbers[adapterPosition]
                // TODO: Handle click event
            }
        }
    }

    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
        holder.bind(numbers[position])
    }

    override fun getItemCount() = numbers.size
}

class NumberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val textView: TextView = itemView.findViewById(R.id.textView)

    fun bind(number: Int) {
        textView.text = number.toString()
    }
}