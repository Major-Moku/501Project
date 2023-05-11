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
import com.example.a501project.api.COD
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

        else if (it == "Call of Duty: Modern Warfare") return "Call of Duty: Modern Warfare is a first-person shooter video game developed by Infinity Ward and published by Activision. It is a reboot of the original Call of Duty 4: Modern Warfare game, released in 2007. The game was released on October 25, 2019, for Microsoft Windows, PlayStation 4, and Xbox One.\n" +
                 "\n" +
                 "Call of Duty: Modern Warfare features a single-player campaign mode that tells a new and more realistic story, focusing on modern-day conflicts and the gray areas of warfare. The game also includes a robust multiplayer mode with various maps, game modes, and customization options. Additionally, there is a cooperative mode called Spec Ops, where players can team up to complete missions and objectives.\n" +
                 "\n" +
                 "The game received positive reviews for its engaging campaign, realistic graphics, and intense multiplayer experience. It is known for its immersive storytelling, high-quality visuals, and tight gunplay mechanics. Call of Duty: Modern Warfare is part of the popular Call of Duty franchise, which has been a major series in the first-person shooter genre for many years."
        else if (it == "Call of Duty: Advanced Warfare") return "Call of Duty: Advanced Warfare is a first-person shooter video game developed by Sledgehammer Games and published by Activision. It was released on November 4, 2014, for Microsoft Windows, PlayStation 4, PlayStation 3, Xbox One, and Xbox 360.\n" +
                 "\n" +
                 "Call of Duty: Advanced Warfare is set in the future, where private military corporations have become powerful forces in global warfare. The game follows the story of Jack Mitchell, a soldier who joins the private military corporation Atlas after losing his arm in combat. The game features a single-player campaign that takes players through various missions and locations around the world, showcasing advanced technology and futuristic weaponry.\n" +
                 "\n" +
                 "One of the key features of Call of Duty: Advanced Warfare is the introduction of the exoskeleton suit, which grants players enhanced mobility, strength, and various abilities. The exoskeleton suit allows for boosted jumps, cloaking, and powerful melee attacks, adding a new layer of gameplay mechanics to the series.\n" +
                 "\n" +
                 "The game also includes a multiplayer mode with various maps, game modes, and customization options. It features the traditional Call of Duty multiplayer experience, but with the addition of exoskeleton abilities, which can be used strategically during matches.\n" +
                 "\n" +
                 "Call of Duty: Advanced Warfare received generally positive reviews from critics, who praised its futuristic setting, engaging campaign, and the introduction of the exoskeleton mechanics. It was considered a significant departure from previous installments in the series, offering a fresh take on the Call of Duty formula.\n"
        else if (it == "Call of Duty: Ghost") return "Call of Duty: Ghosts is a first-person shooter video game developed by Infinity Ward and published by Activision. It was released on November 5, 2013, for Microsoft Windows, PlayStation 4, PlayStation 3, Xbox One, Xbox 360, and Wii U.\n" +
                 "\n" +
                 "Call of Duty: Ghosts is set in a post-apocalyptic future where the United States has been weakened by a catastrophic event. Players assume the role of soldiers in a specialized unit known as \"Ghosts\" who are fighting against a global superpower known as the \"Federation.\" The game's campaign follows the story of the Ghosts as they attempt to repel the Federation's invasion of the United States.\n" +
                 "\n" +
                 "The game features a single-player campaign mode that takes players through various missions in different locations, including war-torn cities, jungles, and even outer space. It introduces new gameplay mechanics, such as the ability to control a military-trained dog named Riley, who assists players in combat.\n" +
                 "\n" +
                 "In addition to the campaign, Call of Duty: Ghosts includes a multiplayer mode with various maps, game modes, and customization options. The multiplayer features many familiar elements from previous Call of Duty games, including a progression system, customizable loadouts, and killstreak rewards.\n" +
                 "\n" +
                 "Call of Duty: Ghosts received mixed reviews from critics. While the game was praised for its graphics and multiplayer features, it received criticism for its lackluster campaign and perceived lack of innovation compared to previous installments in the series. Nonetheless, it remains a notable entry in the Call of Duty franchise and has its own dedicated fan base."
        else if (it == "Call of Duty: Vanguard") return "Call of Duty: Vanguard is an upcoming first-person shooter video game developed by Sledgehammer Games and published by Activision. It is set to be released on November 5, 2021, for Microsoft Windows, PlayStation 4, PlayStation 5, Xbox One, and Xbox Series X/S.\n" +
                 "\n" +
                 "Call of Duty: Vanguard is set during World War II and follows the story of an international group of soldiers known as Task Force One. The game takes players to various locations around the world, including the Eastern Front, the Western Front, the Pacific, and North Africa. It aims to depict the global scope and impact of the war through the eyes of different soldiers from different nations.\n" +
                 "\n" +
                 "The game will feature a single-player campaign that tells a gripping narrative based on real historical events and characters. It will explore the untold stories of World War II and the heroic efforts of the soldiers involved. Additionally, Call of Duty: Vanguard will include a multiplayer mode with a variety of maps, game modes, and customizable options, offering the classic Call of Duty multiplayer experience.\n" +
                 "\n" +
                 "As with previous Call of Duty titles, Vanguard is expected to deliver fast-paced action, intense gunfights, and a wide range of weapons and equipment to utilize during gameplay. It aims to provide an immersive and authentic World War II experience for players."
        else if (it == "Call of Duty: Modern Warfare II")  return "Call of Duty: Modern Warfare II is a 2022 first-person shooter game developed by Infinity Ward and published by Activision. It is a sequel to the 2019 reboot, and serves as the nineteenth installment in the overall Call of Duty series.[2] It was released on October 28, 2022, for the PlayStation 4, PlayStation 5, Windows, Xbox One, and Xbox Series X/S.[3]\n" +
                 "\n" +
                 "Like its predecessor, the game takes place in a realistic and modern setting. The campaign follows multi-national special operations unit Task Force 141 and Mexican Special Forces unit Los Vaqueros as they attempt to track down Iranian Quds Force Major and terrorist Hassan Zyani, who is in possession of American-made ballistic missiles. Powered by a new version of the IW engine, Modern Warfare II continues to support cross-platform multiplayer and also features a free-to-play battle royale mode, Warzone 2.0, a follow-up to the original Warzone.\n" +
                 "\n" +
                 "Modern Warfare II received generally favorable reviews from critics. It was a commercial success and broke several records for the series, including becoming the fastest Call of Duty game to generate US\$1 billion in revenue."
        else if (it == "Call of Duty: Black Ops Cold War") return "Call of Duty: Black Ops Cold War is a first-person shooter video game developed by Treyarch and Raven Software and published by Activision. It was released on November 13, 2020, for Microsoft Windows, PlayStation 4, PlayStation 5, Xbox One, and Xbox Series X/S.\n" +
                 "\n" +
                 "Call of Duty: Black Ops Cold War is the sixth installment in the \"Black Ops\" subseries of the Call of Duty franchise. The game is set during the early 1980s, at the height of the Cold War, and features a fictionalized storyline that takes inspiration from historical events and characters.\n" +
                 "\n" +
                 "In the single-player campaign, players take on the role of a CIA operative and member of the Black Ops team, navigating a complex narrative that involves espionage, covert operations, and global conflicts. The campaign offers multiple branching paths and optional objectives, allowing players to make choices that can impact the story's outcome.\n" +
                 "\n" +
                 "Black Ops Cold War also includes a multiplayer mode with various maps, game modes, and customization options. It features traditional multiplayer gameplay, including team-based battles, objective-based modes, and a progression system that unlocks new weapons, equipment, and perks.\n" +
                 "\n" +
                 "Additionally, the game includes the popular Zombies mode, where players face off against hordes of undead enemies in cooperative gameplay. It offers a unique storyline, diverse maps, and an array of weapons and tools to survive the zombie onslaught.\n" +
                 "\n" +
                 "Call of Duty: Black Ops Cold War received generally positive reviews from critics, praising its campaign's storytelling, multiplayer modes, and the return of the beloved Zombies mode. It is known for its fast-paced action, intense gunfights, and the integration of Cold War-era aesthetics and themes into its gameplay experience."
        else if (it == "Tony Hawk's Pro Skater 1 + 2") return "Tony Hawk's Pro Skater 1 + 2 is a skateboarding video game developed by Vicarious Visions and published by Activision. It is a remastered collection that brings together the first two games in the iconic Tony Hawk's Pro Skater series, which were originally released in 1999 and 2000.\n" +
                 "\n" +
                 "The game features a faithful recreation of the levels, skaters, and gameplay mechanics from the original Tony Hawk's Pro Skater 1 and Tony Hawk's Pro Skater 2, but with updated graphics, improved controls, and additional features. It aims to capture the nostalgia and gameplay experience of the original games while providing a fresh and polished presentation for modern platforms.\n" +
                 "\n" +
                 "In Tony Hawk's Pro Skater 1 + 2, players take on the role of professional skateboarders and perform tricks and stunts in various real-world locations, including iconic skate spots like the Warehouse, School, and Downhill Jam. The game offers a variety of modes, including single-player career modes, multiplayer modes, and online leaderboards.\n" +
                 "\n" +
                 "Players can choose from a roster of professional skaters, including Tony Hawk himself, as well as customize their own skater with different outfits, boards, and accessories. The gameplay revolves around performing tricks, combos, and completing objectives within the time limit to earn points and progress through the game.\n" +
                 "\n" +
                 "The remastered collection received positive reviews from both critics and fans, praising its faithful recreation of the original games, updated visuals, tight controls, and nostalgic appeal. It serves as a celebration of the Tony Hawk's Pro Skater series and has been well-received as a successful revival of the beloved skateboarding franchise."
        else if (it == "Call of Duty: WWII") return "Call of Duty: WWII is a first-person shooter video game developed by Sledgehammer Games and published by Activision. It was released on November 3, 2017, for Microsoft Windows, PlayStation 4, and Xbox One.\n" +
                 "\n" +
                 "Call of Duty: WWII takes players back to the historical setting of World War II. The game's single-player campaign follows the story of Private Ronald \"Red\" Daniels, a young soldier in the U.S. Army's 1st Infantry Division. Players embark on a journey across the European theater of the war, participating in key battles such as the D-Day invasion of Normandy and the Battle of the Bulge.\n" +
                 "\n" +
                 "The game aims to portray the gritty and harrowing nature of the war, highlighting the sacrifices and heroism of the soldiers involved. It explores various aspects of the conflict, including infantry combat, tank warfare, and aerial dogfights.\n" +
                 "\n" +
                 "In addition to the single-player campaign, Call of Duty: WWII features a multiplayer mode with various maps, game modes, and customization options. Players can engage in competitive online matches, team up with friends in cooperative modes, and participate in the popular Nazi Zombies mode, which offers a separate storyline involving supernatural threats.\n" +
                 "\n" +
                 "The game received generally positive reviews from critics, who praised its return to the World War II setting, immersive atmosphere, and compelling single-player campaign. It was applauded for its attention to historical detail and its efforts to deliver an authentic World War II experience. The multiplayer mode was also well-received, offering a range of gameplay options and a return to the series' boots-on-the-ground combat."
        else if (it == "Call of Duty: Black Ops II") return "Call of Duty: Black Ops II is a first-person shooter video game developed by Treyarch and published by Activision. It was released on November 13, 2012, for Microsoft Windows, PlayStation 3, Xbox 360, and Wii U.\n" +
                 "\n" +
                 "Black Ops II is the ninth installment in the Call of Duty franchise and the direct sequel to Call of Duty: Black Ops, which was released in 2010. The game features a campaign mode set in two different time periods: the 1980s and the year 2025. The story follows the character of David Mason, the son of the protagonist from the first Black Ops game, as he investigates and unravels a conspiracy involving cyber warfare, terrorism, and the global balance of power.\n" +
                 "\n" +
                 "The campaign mode of Black Ops II offers branching storylines and player choices that can impact the outcome of the narrative. It introduces a nonlinear structure, allowing players to make decisions that affect the overall storyline and mission objectives.\n" +
                 "\n" +
                 "In addition to the campaign, Black Ops II includes a multiplayer mode with a wide range of maps, game modes, and customization options. It features the traditional Call of Duty multiplayer experience with various competitive modes, progression systems, and killstreak rewards.\n" +
                 "\n" +
                 "The game also introduced the \"Zombies\" mode, a cooperative multiplayer mode where players team up to fight against waves of zombies. It offers its own storyline and unique gameplay mechanics, allowing players to unlock new areas and features as they progress.\n" +
                 "\n" +
                 "Call of Duty: Black Ops II received positive reviews from critics, who praised its engaging campaign, innovative gameplay mechanics, and robust multiplayer mode. It was recognized for its branching narrative and player choice, which added a new layer of depth to the Call of Duty series. The game was a commercial success, further solidifying the Black Ops subseries as one of the most popular within the franchise."
        else if (it == "Call of Duty: Black Ops III") return "Call of Duty: Black Ops III is a first-person shooter video game developed by Treyarch and published by Activision. It was released on November 6, 2015, for Microsoft Windows, PlayStation 4, Xbox One, PlayStation 3, and Xbox 360.\n" +
                 "\n" +
                 "Black Ops III is the twelfth installment in the Call of Duty franchise and the direct sequel to Call of Duty: Black Ops II, which was released in 2012. The game is set in a dystopian future in the year 2065, where advanced technology and cybernetic enhancements have become commonplace.\n" +
                 "\n" +
                 "The campaign of Black Ops III takes place in a world where special operations soldiers known as \"Black Ops\" undergo experimental enhancements to become superhuman warriors. The story follows a group of Black Ops soldiers as they unravel a conspiracy involving rogue AI and the manipulation of human minds.\n" +
                 "\n" +
                 "The campaign mode can be played cooperatively with up to four players, allowing for a cooperative gameplay experience. It features nonlinear missions and a character progression system that allows players to customize their abilities and loadouts.\n" +
                 "\n" +
                 "Black Ops III also includes a robust multiplayer mode with various maps, game modes, and customization options. It offers traditional competitive multiplayer gameplay, including team-based matches, objective-based modes, and a progression system that unlocks new weapons, equipment, and abilities.\n" +
                 "\n" +
                 "In addition to the campaign and multiplayer, Black Ops III features the popular \"Zombies\" mode. It offers a cooperative experience where players fight against waves of undead enemies while uncovering a unique and immersive storyline.\n" +
                 "\n" +
                 "The game received generally positive reviews from critics, who praised its engaging multiplayer, cooperative gameplay, and the depth of the Zombies mode. It was recognized for its futuristic setting, advanced movement mechanics, and the variety of gameplay options it provided to players. Black Ops III was a commercial success and remains a popular entry in the Call of Duty franchise."
        else if (it == "Call of Duty: Modern Warfare Remastered") return "Call of Duty: Modern Warfare Remastered is a first-person shooter video game developed by Raven Software and published by Activision. It is a remastered version of the original Call of Duty 4: Modern Warfare, which was released in 2007.\n" +
                 "\n" +
                 "Modern Warfare Remastered was released on November 4, 2016, as part of special editions of Call of Duty: Infinite Warfare, but it also became available as a standalone game later on. The remastered version features enhanced graphics, improved audio, and other refinements while retaining the core gameplay and storyline of the original game.\n" +
                 "\n" +
                 "The game's campaign follows the story of a small group of British and American special forces soldiers as they combat terrorism across various global hotspots. The campaign offers a compelling narrative and intense missions that tackle themes of modern warfare, terrorism, and geopolitical conflicts.\n" +
                 "\n" +
                 "In addition to the campaign, Modern Warfare Remastered includes a multiplayer mode that retains the iconic maps and gameplay mechanics from the original game. Players can engage in competitive online matches with a wide range of weapons, perks, and killstreak rewards. The multiplayer mode offers a progression system where players can unlock new weapons, attachments, and customization options as they level up.\n" +
                 "\n" +
                 "Modern Warfare Remastered received positive reviews from critics, who praised the game's updated visuals, the faithfulness to the original game, and the enduring quality of its campaign and multiplayer modes. The remaster allowed new players to experience the critically acclaimed Call of Duty 4: Modern Warfare with improved graphics and audio, while also appealing to fans of the original game who wanted to revisit the classic title."


        return "Valorant is a free-to-play first-person tactical shooter video game developed and published by Riot Games. It was released in June 2020 and has quickly gained popularity, particularly in the competitive gaming community.\n" +
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
            }


            else if (gameName == "Valorant"){
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
            else {
                val codServerStatuses = try {
                    COD.getServerStatuses()
                } catch (e: Exception) {
                    emptyMap<String, Boolean>()
                }
                if (gameName == "Call of Duty: Modern Warfare") return codServerStatuses["Call of Duty: Modern Warfare"] ?: false
                else if (gameName == "Call of Duty: Advanced Warfare")  return codServerStatuses["Call of Duty: Advanced Warfare"] ?: false
                else if (gameName == "Call of Duty: Ghost") return codServerStatuses["Call of Duty: Ghost"] ?: false
                else if (gameName == "Call of Duty: Vanguard") return codServerStatuses["Call of Duty: Vanguard"] ?: false
                else if (gameName == "Call of Duty: Modern Warfare II")  return codServerStatuses["Call of Duty: Modern Warfare II"] ?: false
                else if (gameName == "Call of Duty: Black Ops Cold War") return codServerStatuses["Call of Duty: Black Ops Cold War"] ?: false
                else if (gameName == "Tony Hawk's Pro Skater 1 + 2") return codServerStatuses["Tony Hawk's Pro Skater 1 + 2"] ?: false
                else if (gameName == "Call of Duty: WWII") return codServerStatuses["Call of Duty: WWII"] ?: false
                else if (gameName == "Call of Duty: Black Ops II") return codServerStatuses["Call of Duty: Black Ops II"] ?: false
                else if (gameName == "Call of Duty: Black Ops III") return codServerStatuses["Call of Duty: Black Ops III"] ?: false
                else R.drawable.codmwr
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
        else if (it == "Call of Duty: Modern Warfare") R.drawable.codmw
        else if (it == "Call of Duty: Advanced Warfare") R.drawable.codaw
        else if (it == "Call of Duty: Ghost") R.drawable.codg
        else if (it == "Call of Duty: Vanguard") R.drawable.codv
        else if (it == "Call of Duty: Modern Warfare II")  R.drawable.codmw2
        else if (it == "Call of Duty: Black Ops Cold War") R.drawable.codbocw
        else if (it == "Tony Hawk's Pro Skater 1 + 2") R.drawable.tony
        else if (it == "Call of Duty: WWII") R.drawable.codwwii
        else if (it == "Call of Duty: Black Ops II") R.drawable.cowboii
        else if (it == "Call of Duty: Black Ops III") R.drawable.codboiii
        else if (it == "Call of Duty: Modern Warfare Remastered") R.drawable.codmwr
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
