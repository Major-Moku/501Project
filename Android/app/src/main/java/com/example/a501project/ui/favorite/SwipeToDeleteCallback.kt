package com.example.a501project.ui.favorite

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.a501project.ui.adapter.GameAdapter

class SwipeToDeleteCallback(private val adapter: GameAdapter) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // Don't implement anything here because we only want swipe actions
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Remove the item from the data set
        val position = viewHolder.adapterPosition
        adapter.items.removeAt(position)
        adapter.notifyItemRemoved(position)
    }
}
