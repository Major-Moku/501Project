package com.example.a501project.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.a501project.databinding.FragmentProfileBinding
import com.example.a501project.ui.register.RegisterActivity

class ProfileFragment : Fragment() {

    interface OnButtonClickListener{
        fun onButtonClick(newFragment: ProfileEditFragment)
    }

    private var buttonClickListner: OnButtonClickListener? = null

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textNotifications
//        profileViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editButton.setOnClickListener{
            val newFragment = ProfileEditFragment()
            buttonClickListner?.onButtonClick(newFragment)
        }
//        TODO: get content from db and set the edittext.
//        binding.editTextTextPersonName.setText()
//        binding.editTextTextPersonName2.setText()
//        binding.editTextTextPersonName3.setText()
//        binding.editButton.setOnClickListener{
//            supportFragmentManager.beginTransaction()
//                .add(R.id.fragment_container, firstFragment)
//                .commit()
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}