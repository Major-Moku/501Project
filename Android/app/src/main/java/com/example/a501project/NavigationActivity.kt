package com.example.a501project

import android.os.Bundle
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.a501project.databinding.ActivityNavigationBinding
import com.example.a501project.ui.profile.ProfileEditFragment
import com.example.a501project.ui.profile.ProfileFragment
import androidx.fragment.app.Fragment

class NavigationActivity : AppCompatActivity(), ProfileFragment.OnButtonClickListener {

    private lateinit var binding: ActivityNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_navigation)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_favorite, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onButtonClick(newFragment: ProfileEditFragment){
        val currentFragment = supportFragmentManager.findFragmentById(R.id.container)
        if(currentFragment is ProfileFragment){
            val view = currentFragment.view
            view?.findViewById<Button>(R.id.editButton)?.setOnClickListener{
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.container, newFragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }
    }

}