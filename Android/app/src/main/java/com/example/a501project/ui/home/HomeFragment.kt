package com.example.a501project.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a501project.R
import androidx.recyclerview.widget.RecyclerView
import com.example.a501project.databinding.FragmentHomeBinding
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        var isSteamServerOnline = false
        var isOriginServerOnline = false
        lifecycleScope.launch {

            try {
                isSteamServerOnline = withContext(Dispatchers.IO) { Steam.isSteamServerOnline() }
            } catch (e: Exception) {
                Toast.makeText(context, "Error fetching Steam server status: ${e.message}", Toast.LENGTH_LONG).show()
            }

            try {
                isOriginServerOnline = withContext(Dispatchers.IO) { Origin.isOriginServerOnline() }
            } catch (e: Exception) {
                println(e.message)
                Toast.makeText(context, "Error fetching Origin server status: ${e.message}", Toast.LENGTH_LONG).show()
            }

            // Create a list of games with their names and server status
            val gameList = listOf(
                Game("Steam", R.drawable.game_a, isSteamServerOnline),
                Game("Activision", R.drawable.activision, true),
                Game("Origin", R.drawable.game_b, isOriginServerOnline),
                Game("Riot Games", R.drawable.game_c, true),

                //Game("Game E", R.drawable.game_e, false)
            )
            val layoutManager = LinearLayoutManager(requireContext())

            // Find the RecyclerView in your layout
            val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)

            // Create a vertical list of cards using RecyclerView
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = GameAdapter(gameList) { game ->
                val bundle = Bundle().apply { putString("platformName", game.name) }
                findNavController().navigate(
                    R.id.action_homeFragment_to_gameServersFragment,
                    bundle
                )
            }
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
data class Game(val name: String, val imageRes: Int, val isOnline: Boolean)

// Define a RecyclerView adapter for the list of games
class GameAdapter(
    private val gameList: List<Game>,
    private val onGameClick: ((Game) -> Unit)? = null
) : RecyclerView.Adapter<GameAdapter.ViewHolder>() {

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

