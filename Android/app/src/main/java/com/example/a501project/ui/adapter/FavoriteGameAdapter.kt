package com.example.a501project.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.a501project.R
import java.util.*

class FavoriteGameAdapter(val items: MutableList<Game>) : RecyclerView.Adapter<FavoriteGameAdapter.ViewHolder>(), ItemTouchHelperAdapter{

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val imageView: ImageView = itemView.findViewById(R.id.game_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.game_card_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = items[position].name
        holder.imageView.setImageResource(items[position].imageRes)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onItemDismiss(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(items, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }


}

interface ItemTouchHelperAdapter {
    fun onItemDismiss(position: Int)
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
}