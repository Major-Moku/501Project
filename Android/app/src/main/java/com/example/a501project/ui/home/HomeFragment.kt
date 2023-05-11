package com.example.a501project.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a501project.R
import androidx.recyclerview.widget.RecyclerView
import com.example.a501project.databinding.FragmentHomeBinding
import androidx.navigation.fragment.findNavController

import com.example.a501project.ui.adapter.Platform
import com.example.a501project.ui.adapter.PlatformAdapter

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
            val platformList = listOf(
                Platform("Steam", R.drawable.game_a, isSteamServerOnline),
                Platform("Origin", R.drawable.game_b, isOriginServerOnline),
                Platform("Riot Games", R.drawable.game_c, true),
                //Game("Game D", R.drawable.game_a, true),
                //Game("Game E", R.drawable.game_e, false)

            )
            val layoutManager = LinearLayoutManager(requireContext())

            // Find the RecyclerView in your layout
            val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)

            // Create a vertical list of cards using RecyclerView
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = PlatformAdapter(platformList) { platform ->
                val bundle = Bundle().apply { putString("platformName", platform.name) }
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




// Define a RecyclerView adapter for the list of games

