package com.example.a501project.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a501project.R
import com.example.a501project.api.COD
import com.example.a501project.api.Dota2
import com.example.a501project.api.LOL
import com.example.a501project.api.LOR
import com.example.a501project.api.LostArk
import com.example.a501project.api.TFT
import com.example.a501project.api.Valorant
import com.example.a501project.api.Warframe
import csgo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        var recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        recyclerView.adapter = GameAdapter(emptyList())

        var isCSGOServerOnline = false
        var isApexServerOnline = false
        var isDota2ServerOnline = false
        var isWarFrameServerOnline = false
        var isLostArkServerOnline = false
        var isLOLServerOnline = false
        var isLORServerOnline = false
        var isTFTServerOnline = false
        var isValoServerOnline = false
        
        lifecycleScope.launch {

            // Steam
            try {
                isCSGOServerOnline = withContext(Dispatchers.IO) { csgo.isCSGOServerOnline() }
            } catch (e: Exception) {
                Toast.makeText(context, "Error fetching CSGO server status: ${e.message}", Toast.LENGTH_LONG).show()
            }

            try {
                isDota2ServerOnline = withContext(Dispatchers.IO) { Dota2.isDota2ServerOnline() }
            } catch (e: Exception) {
                Toast.makeText(context, "Error fetching Dota 2 server status: ${e.message}", Toast.LENGTH_LONG).show()
            }

            try {
                isWarFrameServerOnline = withContext(Dispatchers.IO) { Warframe.isWarframeServerOnline() }
            } catch (e: Exception) {
                Toast.makeText(context, "Error fetching Warframe server status: ${e.message}", Toast.LENGTH_LONG).show()
            }

            try {
                isLostArkServerOnline = withContext(Dispatchers.IO) { LostArk.isLostArkServerOnline() }
            } catch (e: Exception) {
                Toast.makeText(context, "Error fetching LostArk server status: ${e.message}", Toast.LENGTH_LONG).show()
            }

            // Origin
            try {
                isApexServerOnline = withContext(Dispatchers.IO) { Apex.isApexServerOnline() }
            } catch (e: Exception) {
                Toast.makeText(context, "Error fetching Apex Legends server status: ${e.message}", Toast.LENGTH_LONG).show()
            }

            // Riot
            try {
                isLOLServerOnline = withContext(Dispatchers.IO) { LOL.isLOLServerOnline() }
            } catch (e: Exception) {
                Toast.makeText(context, "Error fetching LOL server status: ${e.message}", Toast.LENGTH_LONG).show()
            }

            try {
                isLORServerOnline = withContext(Dispatchers.IO) { LOR.isLORServerOnline() }
            } catch (e: Exception) {
                Toast.makeText(context, "Error fetching LOR server status: ${e.message}", Toast.LENGTH_LONG).show()
            }

            try {
                isTFTServerOnline = withContext(Dispatchers.IO) { TFT.isTFTServerOnline() }
            } catch (e: Exception) {
                Toast.makeText(context, "Error fetching TFT server status: ${e.message}", Toast.LENGTH_LONG).show()
            }

            try {
                isValoServerOnline = withContext(Dispatchers.IO) { Valorant.isValServerOnline() }
            } catch (e: Exception) {
                Toast.makeText(context, "Error fetching Valorant server status: ${e.message}", Toast.LENGTH_LONG).show()
            }

            val codServerStatuses = try {
                COD.getServerStatuses()
            } catch (e: Exception) {
                emptyMap<String, Boolean>()
            }

            val steamServerList = listOf(
                Game("CSGO", R.drawable.csgo, isCSGOServerOnline),
                Game("Dota 2", R.drawable.dota2, isDota2ServerOnline),
                Game("Warframe", R.drawable.warframe, isWarFrameServerOnline),
                Game("Lost Ark", R.drawable.lostark, isLostArkServerOnline)
                // Add more Steam game servers here
            )

            val originServerList = listOf(
                Game("Apex Legends", R.drawable.apex, isApexServerOnline)
                // Add more Origin game servers here
            )

            val riotServerList = listOf(
                Game("League of Legends", R.drawable.lol, isLOLServerOnline),
                Game("Legends of Runeterra", R.drawable.lor, isLORServerOnline),
                Game("Teamfight Tactics", R.drawable.tft, isTFTServerOnline),
                Game("Valorant", R.drawable.valo, isValoServerOnline)
            )

            val activisionServerList = listOf(
                Game("Call of Duty: Modern Warfare", R.drawable.codmw, codServerStatuses["Call of Duty: Modern Warfare"] ?: false),
                Game("Call of Duty: Advanced Warfare", R.drawable.codaw, codServerStatuses["Call of Duty: Advanced Warfare"] ?: false),
                Game("Call of Duty: Ghost", R.drawable.codg, codServerStatuses["Call of Duty: Ghosts"] ?: false),
                Game("Call of Duty: Vanguard", R.drawable.codv, codServerStatuses["Call of Duty: Vanguard"] ?: false),
                Game("Call of Duty: Modern Warfare II", R.drawable.codmw2, codServerStatuses["Call of Duty: Modern Warfare II"] ?: false),
                Game("Call of Duty: Black Ops Cold War", R.drawable.codbocw, codServerStatuses["Call of Duty: Black Ops Cold War"] ?: false),
                Game("Tony Hawk's Pro Skater 1 + 2", R.drawable.tony, codServerStatuses["Tony Hawk's Pro Skater 1 + 2"] ?: false),
                Game("Call of Duty: WWII", R.drawable.codwwii, codServerStatuses["Call of Duty: WWII"] ?: false),
                Game("Call of Duty: Black Ops II", R.drawable.cowboii, codServerStatuses["Call of Duty: Black Ops II"] ?: false),
                Game("Call of Duty: Black Ops III", R.drawable.codboiii, codServerStatuses["Call of Duty: Black Ops III"] ?: false),
                Game("Call of Duty: Modern Warfare Remastered", R.drawable.codmwr, codServerStatuses["Call of Duty: Modern Warfare Remastered"] ?: false)
            )

            // Choose the appropriate server list based on the platform name
            val gameServerList = when (platformName) {
                "Steam" -> steamServerList
                "Activision" -> activisionServerList
                "Origin" -> originServerList
                "Riot Games" -> riotServerList
                else -> emptyList<Game>()
            }

            withContext(Dispatchers.Main) {
                recyclerView.adapter = GameAdapter(gameServerList)
            }

            /*
            val layoutManager = LinearLayoutManager(requireContext())
            val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = GameAdapter(gameServerList)
            recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), layoutManager.orientation))
            */
        }

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
