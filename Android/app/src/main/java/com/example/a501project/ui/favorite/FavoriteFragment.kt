package com.example.a501project.ui.favorite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a501project.R
import com.example.a501project.data.Game
import com.example.a501project.databinding.FragmentFavoriteBinding
import com.example.a501project.ui.RecyclerItemClickListener
import com.example.a501project.ui.adapter.GameAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.FormBody
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

        //TODO http request favorite list

        GlobalScope.launch(Dispatchers.IO) {
            val requestBody = FormBody.Builder()
                .add("username", "Yan3")
                .build()

            val request = Request.Builder()
                .url("https://cs501andriodsquad.com:4567/api/user/favoriteGames" + "?username=" + "Yan3")
                .get()
                .build()

            val response = HttpClient.instance.newCall(request).execute()
            val responseBody = response.body?.string()
            val gson = Gson()
            val type = object : TypeToken<List<String>>() {}.type
            val favoritaGames = gson.fromJson<List<String>>(responseBody, type)

            print(favoritaGames)
            
            withContext(Dispatchers.Main) {

                val myObjects = favoritaGames.map { Game(it, getMyDrawable(it)) }.toMutableList()

                recyclerView.adapter = GameAdapter(myObjects)
                // Add swipe to delete
                val swipeToDeleteCallback = SwipeToDeleteCallback(recyclerView.adapter!! as GameAdapter)
                val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
                itemTouchHelper.attachToRecyclerView(recyclerView)
                val itemClickListener = myActivity.let {
                    RecyclerItemClickListener(
                        it, recyclerView,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                print(myObjects[position])
                                // TODO http request, go to game fragment
                                println("lalala")
                            }
                        })
                }
                if (itemClickListener != null) {
                    recyclerView.addOnItemTouchListener(itemClickListener)
                }
            }
        }




        return root
    }

    private fun getMyDrawable(it: String): Int {
        if (it == "LOL") return R.drawable.civil
        else if (it == "Gensin Impact") return R.drawable.overcooked
        else return R.drawable.human_fall_flat
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}