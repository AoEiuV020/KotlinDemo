package cc.aoeiuv020.demo

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_coordinator.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.ctx
import org.jetbrains.anko.startActivity

class CoordinatorActivity : AppCompatActivity(), AnkoLogger {
    companion object {
        fun start(ctx: Context) {
            ctx.startActivity<CoordinatorActivity>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordinator)

        val llm = LinearLayoutManager(ctx)
        rvContent.layoutManager = llm
        rvContent.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                return object : RecyclerView.ViewHolder(TextView(ctx)) {}
            }

            override fun getItemCount(): Int {
                return 2
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                (holder.itemView as TextView).text = "$position"
            }
        }
    }
}
