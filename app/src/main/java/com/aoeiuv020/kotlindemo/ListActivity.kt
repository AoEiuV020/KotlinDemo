package com.aoeiuv020.kotlindemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListActivity : SwipeBackActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)!!
        recyclerView.layoutManager = LinearLayoutManager(this)
        val numbers = (1..100).toList()
        recyclerView.adapter = NumberAdapter(numbers)
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