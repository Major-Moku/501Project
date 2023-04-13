package com.example.a501project.ui.register

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a501project.NavigationActivity
import com.example.a501project.databinding.RegisterBinding

class RegisterActivity : AppCompatActivity(){
        private lateinit var binding:RegisterBinding
        private var registerSuccess = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener{
            // submit the result to the server and set the registerSuccess
            if(!registerSuccess){
                Toast.makeText(this,
                    "Success!",
                    Toast.LENGTH_SHORT).show()

                val intent = Intent(this, NavigationActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this,
                    "Failed, please reenter the information.",
                    Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun getName(): String {
        return binding.PersonNameForRegister.text.toString()
    }

    private fun getPwd():String{
        return binding.PasswordForRegister.text.toString()
    }
}