package wxgaly.android.jetpacksample.widget

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpandableRecyclerViewAdapter : RecyclerView.Adapter<ExpandableViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpandableViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ExpandableViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}

class ExpandableViewHolder internal constructor(private val expandableView: View) :
    RecyclerView.ViewHolder(expandableView) {

    internal fun bind(text: String) {

    }
}