package cc.aoeiuv020.demo

import android.content.Context
import android.view.View
import android.widget.TextView
import co.ceryle.fitgridview.FitGridAdapter

class MyFitGridAdapter(context: Context, row: Int, column: Int) : FitGridAdapter(context, R.layout.item_fit_grid_view, row, column) {

    override fun onBindView(position: Int, view: View) {
        (view as TextView).text = position.toString()
    }
}
