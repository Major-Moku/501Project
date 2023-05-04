package com.example.a501project.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a501project.R

class GameServersFragment : Fragment() {

    private lateinit var platformName: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        platformName = arguments?.getString("platformName") ?: ""
        setHasOptionsMenu(true) // Enable options menu
        return inflater.inflate(R.layout.fragment_game_servers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create lists of game servers for different platforms
        val steamServerList = listOf(
            Game("CSGO", R.drawable.csgo, true),
            Game("DOTA 2", R.drawable.dota2, false)
            // Add more Steam game servers here
        )

        val originServerList = listOf(
            Game("Apex Legends", R.drawable.apex, true),
            Game("FIFA", R.drawable.fifa23, true)
            // Add more Origin game servers here
        )

        // Choose the appropriate server list based on the platform name
        val gameServerList = when (platformName) {
            "Steam" -> steamServerList
            "Origin" -> originServerList
            else -> emptyList<Game>()
        }

        val layoutManager = LinearLayoutManager(requireContext())
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = GameAdapter(gameServerList)
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), layoutManager.orientation))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Navigate back to HomeFragment when the back button is clicked
                findNavController().popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
