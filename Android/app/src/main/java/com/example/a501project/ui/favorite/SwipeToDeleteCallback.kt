package com.example.a501project.ui.favorite

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.a501project.data.CurrentUser
import com.example.a501project.ui.adapter.GameAdapter
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Request

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

    @OptIn(DelicateCoroutinesApi::class)
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Remove the item from the data set
        val position = viewHolder.adapterPosition
        GlobalScope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url("https://cs501andriodsquad.com:4567/api/user/deletegame" + "?username=" + CurrentUser.username + "&gamename=" + adapter.items[position].name)
                .delete()
                .build()

            val response = HttpClient.instance.newCall(request).execute()
            val responseBody = response.body?.string()

            withContext(Dispatchers.Main) {
                if (response.code == 200) {
                    adapter.items.removeAt(position)
                    adapter.notifyItemRemoved(position)
                }

            }
        }




    }
}
