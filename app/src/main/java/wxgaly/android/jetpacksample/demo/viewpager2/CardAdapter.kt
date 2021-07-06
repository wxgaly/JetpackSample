package wxgaly.android.viewpager2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import wxgaly.android.jetpacksample.R

class CardAdapter(private var list: List<String>) : RecyclerView.Adapter<CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.viewpager2_item_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}

class CardViewHolder internal constructor(private val cardView: View) :
    RecyclerView.ViewHolder(cardView) {

    internal fun bind(text: String) {
        cardView.findViewById<TextView>(R.id.tv_item_name).text = text
    }
}