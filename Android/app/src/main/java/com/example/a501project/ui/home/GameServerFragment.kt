package com.example.a501project.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a501project.R
import com.example.a501project.ui.RecyclerItemClickListener
import com.example.a501project.ui.adapter.Game
import com.example.a501project.ui.adapter.GameAdapter

class GameServersFragment : Fragment() {

    private lateinit var platformName: String

    private lateinit var myActivity: AppCompatActivity // Declare a reference to the activity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AppCompatActivity) {
            myActivity = context // Assign the activity reference
        }
    }

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


        val steamServerList = listOf(
            Game("CSGO", R.drawable.csgo ),
            Game("Dota 2", R.drawable.dota2),
            Game("Warframe", R.drawable.warframe),
            Game("Lost Ark", R.drawable.lostark)
            // Add more Steam game servers here
        ).toMutableList()

        val originServerList = listOf(
            Game("Apex Legends", R.drawable.apex)
            // Add more Origin game servers here
        ).toMutableList()


        val riotServerList = listOf(
            Game("League of Legends", R.drawable.lol),
            Game("Legends of Runeterra", R.drawable.lor),
            Game("Teamfight Tactics", R.drawable.tft),
            Game("Valorant", R.drawable.valo)
        ).toMutableList()

        val activisionServerList = listOf(
            Game("Call of Duty: Modern Warfare", R.drawable.codmw),
            Game("Call of Duty: Advanced Warfare", R.drawable.codaw),
            Game("Call of Duty: Ghost", R.drawable.codg),
            Game("Call of Duty: Vanguard", R.drawable.codv),
            Game("Call of Duty: Modern Warfare II", R.drawable.codmw2),
            Game("Call of Duty: Black Ops Cold War", R.drawable.codbocw),
            Game("Tony Hawk's Pro Skater 1 + 2", R.drawable.tony),
            Game("Call of Duty: WWII", R.drawable.codwwii),
            Game("Call of Duty: Black Ops II", R.drawable.cowboii),
            Game("Call of Duty: Black Ops III", R.drawable.codboiii),
            Game("Call of Duty: Modern Warfare Remastered", R.drawable.codmwr)
        ).toMutableList()

        // Choose the appropriate server list based on the platform name
        val gameServerList = when (platformName) {
            "Steam" -> steamServerList
            "Origin" -> originServerList
            "Riot Games" -> riotServerList
            "Activision" -> activisionServerList
            else -> emptyList<Game>().toMutableList()
        }



        val layoutManager = LinearLayoutManager(requireContext())
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = GameAdapter(gameServerList as MutableList<Game>)
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), layoutManager.orientation))

        val itemClickListener = myActivity.let {
            RecyclerItemClickListener(
                it, recyclerView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val bundle = Bundle().apply { putString("gameName", gameServerList[position].name) }
                        findNavController().navigate(
                            R.id.action_gameServersFragment_to_gameDetailFragment,
                            bundle
                        )
                    }
                })
        }
        recyclerView.addOnItemTouchListener(itemClickListener)

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
