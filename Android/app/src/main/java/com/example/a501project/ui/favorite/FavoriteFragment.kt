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

//        GlobalScope.launch(Dispatchers.IO) {
//            val requestBody = FormBody.Builder()
//                .add("username", "Yan3")
//                .build()
//
//            val request = Request.Builder()
//                .url("https://34.130.240.157:4567/api/user/favoriteGames")
//                .post(requestBody)
//                .get()
//                .build()
//
//            val response = HttpClient.instance.newCall(request).execute()
//            val responseBody = response.body?.string()
//
//            // process the response in the background thread
//            // ...
//
//            // update the UI in the main thread (if needed)
//            withContext(Dispatchers.Main) {
//                // update the UI
//                // ...
//            }
//        }


        val myObjects = mutableListOf(
            Game("Item 1", R.drawable.civil),
            Game("Item 2", R.drawable.overcooked),
            Game("Item 3", R.drawable.human_fall_flat)
        )

        recyclerView.adapter = GameAdapter(myObjects)

        // Add swipe to delete
        val swipeToDeleteCallback = SwipeToDeleteCallback(recyclerView.adapter!! as GameAdapter)
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        val itemClickListener = this.context?.let {
            RecyclerItemClickListener(
                it, recyclerView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        // TODO http request, go to game fragment
                        println("lalala")
                    }
                })
        }
        if (itemClickListener != null) {
            recyclerView.addOnItemTouchListener(itemClickListener)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}