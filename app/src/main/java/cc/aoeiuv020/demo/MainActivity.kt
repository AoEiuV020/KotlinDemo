package cc.aoeiuv020.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnListView.setOnClickListener {
            ListViewActivity.start(this)
        }

        btnNested.setOnClickListener {
            NestedActivity.start(this)
        }

        btnGrid.setOnClickListener {
            GridActivity.start(this)
        }

        btnPagerGrid.setOnClickListener {
            PagerGridActivity.start(this)
        }

        btnCoordinator.setOnClickListener {
            CoordinatorActivity.start(this)
        }

        btnGridItemHeight.setOnClickListener {
            GridItemHeightActivity.start(this)
        }
    }
}
