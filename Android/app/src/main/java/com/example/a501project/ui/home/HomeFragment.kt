package com.example.a501project.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a501project.R
import androidx.recyclerview.widget.RecyclerView
import com.example.a501project.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create a list of games with their names and server status
        val gameList = listOf(
            Game("Steam", R.drawable.game_a, true),
            Game("Origin", R.drawable.game_b, false),
            //Game("Game C", R.drawable.game_c, true),
            //Game("Game D", R.drawable.game_d, true),
            //Game("Game E", R.drawable.game_e, false)
        )
        val layoutManager = LinearLayoutManager(requireContext())

        // Find the RecyclerView in your layout
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)

        // Create a vertical list of cards using RecyclerView
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = GameAdapter(gameList)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
data class Game(val name: String, val imageRes: Int, val isOnline: Boolean)

// Define a RecyclerView adapter for the list of games
class GameAdapter(private val gameList: List<Game>) :
    RecyclerView.Adapter<GameAdapter.ViewHolder>() {

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
