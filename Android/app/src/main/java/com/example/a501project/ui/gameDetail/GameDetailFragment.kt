package com.example.a501project.ui.home

import Apex
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.a501project.R
import com.example.a501project.api.Dota2
import com.example.a501project.api.LOL
import com.example.a501project.api.LOR
import com.example.a501project.api.LostArk
import com.example.a501project.api.TFT
import com.example.a501project.api.Valorant
import com.example.a501project.api.Warframe
import com.example.a501project.data.CurrentUser
import com.example.a501project.databinding.FragmentGameDetailBinding
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.post
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class GameDetailFragment : Fragment() {


    private var _binding: FragmentGameDetailBinding? = null

    private val binding get() = _binding!!

    private lateinit var gameName: String

    private lateinit var myActivity: AppCompatActivity // Declare a reference to the activity

    private val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AppCompatActivity) {
            myActivity = context // Assign the activity reference
        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        gameName = arguments?.getString("gameName") ?: ""
        inflater.inflate(R.layout.fragment_game_detail, container, false)
        setHasOptionsMenu(true) // Enable options menu

        val button = binding.subscribe
        button.setOnClickListener {

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    try {
                        val url = "https://cs501andriodsquad.com:4567/api/user/addgame?username=${CurrentUser.username}&gamename=$gameName"
                        client.post<Unit>(url)
                        Log.d("GameAdapter", "Successfully added game $gameName to favorites")

                    } catch (e: Exception) {
                        // Handle error
                        Log.d("favorite game","fail to add a game")
                    }

                } catch (e: Exception) {
                    // Handle error
                    Log.d("favorite game","fail to add a game")
                }
            }
        }
        val status = binding.statusIcon
        runBlocking {
            if (checkServerStatus(gameName)) {
                Log.d("haha", "success")

                status.setImageResource(R.drawable.ic_online)
                status.isVisible = true


            } else {
                status.setImageResource(R.drawable.ic_offline)
                status.isVisible = true

                Log.d("haha", "failed")
            }
        }

        val image = binding.gameImg
        image.setImageResource(getMyDrawable(gameName))

        val description = binding.description
        description.text = getDescription(gameName)






        return root
    }

    private fun getDescription(it: String): String {
         if (it == "CSGO") return "CSGO stands for Counter-Strike: Global Offensive. It is a popular first-person shooter (FPS) video game developed by Valve Corporation and Hidden Path Entertainment. CSGO is the fourth installment in the Counter-Strike series, which originated as a modification for the game Half-Life.\n" +
                "\n" +
                "In CSGO, players join either the terrorist team or the counter-terrorist team and compete in various game modes and maps. The objective varies depending on the mode, but typically includes planting or defusing bombs, rescuing hostages, or eliminating the opposing team."
        else if (it == "Dota 2") return "Dota 2, short for Defense of the Ancients 2, is a highly popular multiplayer online battle arena (MOBA) video game developed and published by Valve Corporation. It is the sequel to the original Dota, which was a community-created mod for Warcraft III: Reign of Chaos.\n" +
                "\n" +
                "In Dota 2, two teams of five players each compete against each other, with the ultimate goal of destroying the enemy team's Ancient, a heavily guarded structure located in their base. Each player controls a single hero character with unique abilities and attributes, and they work together to push lanes, secure objectives, and defeat enemy heroes."
        else if (it == "Warframe") return "Warframe is a free-to-play online cooperative third-person shooter video game developed and published by Digital Extremes. It was initially released in 2013 and has since gained a significant player base and ongoing updates.\n" +
                 "\n" +
                 "In Warframe, players control members of the Tenno, a race of ancient warriors awakened from centuries of cryosleep. The game is set in a science fiction universe where players take on the role of these advanced space ninjas known as Warframes. Each Warframe has unique abilities and playstyles, allowing players to customize their gameplay experience.\n" +
                 ""
        else if (it == "Lost Ark") return "Lost Ark is a massively multiplayer online role-playing game (MMORPG) developed by Smilegate RPG and published by Smilegate. It was initially released in South Korea in 2018 and has gained significant attention and popularity.\n" +
                 "\n" +
                 "Lost Ark is set in a vibrant and expansive fantasy world with various regions and continents to explore. Players assume the role of adventurers known as \"Arkans\" who embark on epic quests and engage in thrilling combat against powerful foes."
        else if (it == "Apex Legends") return "Apex Legends is a free-to-play battle royale video game developed by Respawn Entertainment and published by Electronic Arts. It was released in February 2019 and quickly gained popularity among players worldwide.\n" +
                 "\n" +
                 "Apex Legends is set in the futuristic universe of the Titanfall series, although it does not feature the series' signature Titans (giant mechs). Instead, it focuses on a fast-paced, team-based gameplay experience."
        else if (it == "League of Legends") return "League of Legends (LoL) is a free-to-play multiplayer online battle arena (MOBA) video game developed and published by Riot Games. It was first released in 2009 and has since become one of the most popular and influential games in the esports industry.\n" +
                 "\n" +
                 "In League of Legends, two teams of five players each compete against each other on a symmetrical map known as the Summoner's Rift. The objective is to destroy the enemy team's Nexus, a structure located in their base, while defending your own. Players control characters called \"champions,\" each with their own unique abilities, playstyles, and roles within the team."
        else if (it == "Legends of Runeterra") return "Legends of Runeterra is a free-to-play digital collectible card game (CCG) developed and published by Riot Games. It is set in the same universe as the popular MOBA game League of Legends (LoL) and features characters and lore from the world of Runeterra.\n" +
                 "\n" +
                 "In Legends of Runeterra, players build decks of cards representing champions, spells, and followers. Each card has its own unique abilities and strategic value. The objective is to strategically outmaneuver and defeat the opponent by reducing their Nexus (similar to a health pool) to zero."
        else if (it == "Teamfight Tactics") return "Teamfight Tactics (TFT) is an auto-battler game mode developed and published by Riot Games. It was initially released as a game mode within League of Legends but has since become a standalone game available on multiple platforms.\n" +
                 "\n" +
                 "In Teamfight Tactics, players compete against each other in a strategic auto-battle arena. The game revolves around assembling and managing a team of champions to battle against other players' teams. However, unlike traditional MOBA gameplay, the battles are automated, and players focus on team composition, positioning, and strategic decision-making."

        else return "Valorant is a free-to-play first-person tactical shooter video game developed and published by Riot Games. It was released in June 2020 and has quickly gained popularity, particularly in the competitive gaming community.\n" +
                 "\n" +
                 "In Valorant, two teams of five players each compete against each other in matches that take place across different maps. The objective varies depending on the game mode but typically involves one team attacking and trying to plant a bomb called the Spike, while the other team defends and attempts to prevent the bomb from being planted or defuse it if it is planted."
    }

    private suspend fun checkServerStatus(gameName: String) : Boolean {
            if (gameName == "CSGO") {
                try {
                    return Dota2.isDota2ServerOnline()
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Error fetching Dota 2 server status: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } else if (gameName == "Dota 2") {
                try {
                    return Dota2.isDota2ServerOnline()
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Error fetching Dota 2 server status: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            else if (gameName == "Warframe") {
                try {
                    return Warframe.isWarframeServerOnline()
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Error fetching Warframe server status: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            else if (gameName == "Lost Ark") {
                try {
                    return LostArk.isLostArkServerOnline()
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Error fetching LostArk server status: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            else if (gameName == "Apex Legends") {
                try {
                    return Apex.isApexServerOnline()
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Error fetching Apex Legends server status: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            else if (gameName == "League of Legends") {
                try {
                    return LOL.isLOLServerOnline()
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Error fetching LOL server status: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
            else if (gameName == "Legends of Runeterra") {
                try {
                    return LOR.isLORServerOnline()
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Error fetching LOR server status: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            else if (gameName == "Teamfight Tactics") {

                try {
                    return TFT.isTFTServerOnline()
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Error fetching TFT server status: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else  {
                try {
                     return Valorant.isValServerOnline()
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Error fetching Valorant server status: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        return false
    }


    private fun getMyDrawable(it: String): Int {
        return if (it == "CSGO") R.drawable.csgo
        else if (it == "Dota 2") R.drawable.dota2
        else if (it == "Warframe") R.drawable.warframe
        else if (it == "Lost Ark") R.drawable.lostark
        else if (it == "Apex Legends") R.drawable.apex
        else if (it == "League of Legends") R.drawable.lol
        else if (it == "Legends of Runeterra") R.drawable.lor
        else if (it == "Teamfight Tactics") R.drawable.tft

        else R.drawable.valo
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
