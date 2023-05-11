package com.example.a501project.ui.favorite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a501project.R
import com.example.a501project.data.CurrentUser
import com.example.a501project.databinding.FragmentFavoriteBinding
import com.example.a501project.ui.adapter.FavoriteGameAdapter
import com.example.a501project.ui.adapter.Game
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Request

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private lateinit var myActivity: AppCompatActivity // Declare a reference to the activity

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
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // set up the favorite list
        val recyclerView = binding.favoriteRv
        val linearLayoutManager = LinearLayoutManager(myActivity)

        recyclerView.layoutManager = linearLayoutManager

        GlobalScope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url("https://cs501andriodsquad.com:4567/api/user/favoriteGames" + "?username=" + CurrentUser.username)
                .get()
                .build()

            val response = HttpClient.instance.newCall(request).execute()
            val responseBody = response.body?.string()
            val gson = Gson()
            val type = object : TypeToken<List<String>>() {}.type
            val favoritaGames = gson.fromJson<List<String>>(responseBody, type)
            
            withContext(Dispatchers.Main) {

                val myObjects = favoritaGames.map { Game(it, getMyDrawable(it) as Int) }.toMutableList()

                recyclerView.adapter = FavoriteGameAdapter(myObjects)
                val swipeToDeleteCallback = SwipeToDeleteCallback(recyclerView.adapter!! as FavoriteGameAdapter)
                val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
                itemTouchHelper.attachToRecyclerView(recyclerView)
                // Add swipe to delete
                recyclerView.addOnItemTouchListener(
                    SwipeOrClickListener(
                        context = myActivity,
                        onSwipe = { position ->
                            // Handle swipe gesture for the item at the given position
                            // You can access the adapter and data to perform any desired actions
                            GlobalScope.launch(Dispatchers.IO) {
                                val request = Request.Builder()
                                    .url("https://cs501andriodsquad.com:4567/api/user/deletegame" + "?username=" + CurrentUser.username + "&gamename=" + (recyclerView.adapter as FavoriteGameAdapter).items[position].name)
                                    .delete()
                                    .build()

                                val response = HttpClient.instance.newCall(request).execute()
                                val responseBody = response.body?.string()

                                withContext(Dispatchers.Main) {
                                    if (response.code == 200) {
                                        myObjects.removeAt(position)
                                        (recyclerView.adapter as FavoriteGameAdapter).notifyItemRemoved(position)
                                    }

                                }
                            }
                            // ...
                        },
                        onClick = { position ->
                            // Handle click gesture for the item at the given position
                            // You can access the adapter and data to perform any desired actions
                            val bundle = Bundle().apply { putString("gameName", myObjects[position].name) }
                                                                        findNavController().navigate(
                                                                            R.id.action_favoriteFragment_to_gameDetailFragment,
                                                                            bundle)
                            // ...
                        }
                    )
                )
//                val itemClickListener = myActivity.let {
//                    RecyclerItemClickListener(
//                        it, recyclerView,
//                        object : RecyclerView.OnItemTouchListener {
//                            private val gestureDetector: GestureDetector
//
//                            init {
//                                gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
//                                    override fun onDoubleTap(e: MotionEvent, position: Int): Boolean {
//                                        val bundle = Bundle().apply { putString("gameName", myObjects[position].name) }
//                                                                        findNavController().navigate(
//                                                                            R.id.action_favoriteFragment_to_gameDetailFragment,
//                                                                            bundle)
//
//                                    }
//                                })
//                            }
//
//                            override fun onInterceptTouchEvent(
//                                rv: RecyclerView,
//                                e: MotionEvent
//                            ): Boolean {
//                                TODO("Not yet implemented")
//                            }
//
//                            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
//                                TODO("Not yet implemented")
//                            }
//
//                            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
//                                TODO("Not yet implemented")
//                            }
//                        })
//                }

            }
        }

        return root
    }

    private fun getMyDrawable(it: String): Any {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}