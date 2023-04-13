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
import androidx.recyclerview.widget.RecyclerView
import com.example.a501project.R
import com.example.a501project.data.Game
import com.example.a501project.databinding.FragmentFavoriteBinding
import com.example.a501project.ui.adapter.GameAdapter

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
        val myObjects = mutableListOf(
            Game("Item 1", R.drawable.civil),
            Game("Item 2", R.drawable.overcooked),
            Game("Item 3", R.drawable.human_fall_flat)
        )

        recyclerView.adapter = GameAdapter(myObjects)

        // Add swipe to delete
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // Do nothing
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Remove the item from the data set
                val position = viewHolder.adapterPosition
                myObjects.removeAt(position)
                recyclerView.adapter?.notifyItemRemoved(position)
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}