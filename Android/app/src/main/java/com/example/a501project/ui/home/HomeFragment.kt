package com.example.a501project.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.a501project.databinding.FragmentHomeBinding
import androidx.appcompat.app.AppCompatActivity
import com.example.a501project.R

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var gridLayout: ConstraintLayout
    private lateinit var generateButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view: View = binding.root

        gridLayout = binding.gridLayout
        generateButton = binding.buttonGenerate

        generateButton.setOnClickListener {
            generateGrid(4, 4) // Replace the numbers with the desired number of rows and columns.
        }

        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
        return view
    }

    private fun generateGrid(rows: Int, columns: Int) {
        gridLayout.removeAllViews()

        val constraintSet = ConstraintSet()
        var lastId: Int = View.NO_ID

        for (i in 0 until rows) {
            var lastRowId: Int = View.NO_ID

            for (j in 0 until columns) {
                val view = View(requireContext()).apply {
                    id = View.generateViewId()
                    setBackgroundColor(
                        resources.getColor(
                            android.R.color.holo_blue_light,
                            requireContext().theme
                        )
                    )
                    layoutParams = ConstraintLayout.LayoutParams(0, 0)
                }

                gridLayout.addView(view)
                constraintSet.clone(gridLayout)

                val topMargin = if (i == 0) 8 else 16
                val startMargin = if (j == 0) 8 else 16

                if (lastRowId != View.NO_ID) {
                    constraintSet.connect(
                        view.id,
                        ConstraintSet.START,
                        lastRowId,
                        ConstraintSet.END,
                        startMargin
                    )
                } else {
                    constraintSet.connect(
                        view.id,
                        ConstraintSet.START,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.START,
                        startMargin
                    )
                }
                if (lastId != View.NO_ID) {
                    constraintSet.connect(view.id, ConstraintSet.TOP, lastId, ConstraintSet.BOTTOM, topMargin)
                } else {
                    constraintSet.connect(view.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, topMargin)
                }
                constraintSet.constrainWidth(view.id, 0)
                constraintSet.constrainHeight(view.id, 0)
                constraintSet.constrainedWidth(view.id, true)
                constraintSet.constrainedHeight(view.id, true)
                constraintSet.setHorizontalWeight(view.id, 1f)
                constraintSet.setVerticalWeight(view.id, 1f)

                constraintSet.applyTo(gridLayout)

                lastRowId = view.id
            }
            lastId = lastRowId
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}