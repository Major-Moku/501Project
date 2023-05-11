package com.example.a501project.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a501project.R

class PlatformAdapter(
    private val gameList: List<Platform>,
    private val onGameClick: ((Platform) -> Unit)? = null
    ) : RecyclerView.Adapter<PlatformAdapter.ViewHolder>() {

        // Inflate the layout for each item in the list
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_game, parent, false)
            return ViewHolder(view)
        }

        // Set the data for each item in the list
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val game = gameList[position]
            holder.gameName.text = game.name
            holder.gameImage.setImageResource(game.imageRes)
            if (game.isOnline) {
                holder.statusIcon.setImageResource(R.drawable.ic_online)
            } else {
                holder.statusIcon.setImageResource(R.drawable.ic_offline)
            }

            // Set the click listener for the item
            holder.itemView.setOnClickListener {
                onGameClick?.invoke(game)
            }
        }

        override fun getItemCount(): Int {
            return gameList.size
        }

        // Define a ViewHolder to hold the views for each item in the list
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val gameName: TextView = view.findViewById(R.id.game_name)
            val gameImage: ImageView = view.findViewById(R.id.game_image)
            val statusIcon: ImageView = view.findViewById(R.id.status_icon)
        }
    }

data class Platform(val name: String, val imageRes: Int, val isOnline: Boolean)
