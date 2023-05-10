package com.example.a501project.ui.register

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a501project.NavigationActivity
import com.example.a501project.databinding.RegisterBinding
import com.example.a501project.ui.login.LoginActivity
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class RegisterActivity : AppCompatActivity(){
        private lateinit var binding:RegisterBinding
        private var registerSuccess = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonForRegister.setOnClickListener{
            // submit the result to the server and set the registerSuccess
//            if(!registerSuccess){
//                Toast.makeText(this,
//                    "Success!",
//                    Toast.LENGTH_SHORT).show()
//
//                val intent = Intent(this, NavigationActivity::class.java)
//                startActivity(intent)
//            }else{
//                Toast.makeText(this,
//                    "Failed, please reenter the information.",
//                    Toast.LENGTH_SHORT).show()
//            }
            val username = getName()
            val password = getPwd()

                // Validate text field value of username
            val isValidUsername = validateUsername(username)
            if (!isValidUsername) return@setOnClickListener

                // Validate text field value of password
            val isValidPassword = validatePassword(password)
            if (!isValidPassword) return@setOnClickListener

                // Query the database to see if the user is registered

            val result = queryDB(username)
            CoroutineScope(Dispatchers.Main).launch{
                val response = result.await()
                if (response.equals("User not found.")){
                    val updateResult = updateDB(username,password).await()
//                    println(updateResult)
                    if(updateResult.equals("New user added successfully.")){
                        Toast.makeText(this@RegisterActivity,"New user added successfully. Please relogin.",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this@RegisterActivity,updateResult,Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this@RegisterActivity,"user already exist, please use another username.",Toast.LENGTH_SHORT).show()
                }
            }


//            val isUserExist = isRegistered(username)
//            if (isUserExist) {
//                Toast.makeText(this, "User already exists. Please login or pick another username.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            updateDB(username, password)
//            Toast.makeText(this,
//            "Success! Please relogin.",
//                Toast.LENGTH_LONG).show()
//
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)

        }
    }

    private fun getName(): String {
        return binding.PersonNameForRegister.text.toString()
    }

    private fun getPwd():String{
        return binding.PasswordForRegister.text.toString()
    }

    private fun validatePassword(password : String): Boolean {
        // 1. A valid password should not be empty
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password must not be empty", Toast.LENGTH_SHORT).show()
            return false
        }

        // 2. A valid password should be between 3 and 16 characters long
        if (password.length < 3 || password.length > 16) {
            Toast.makeText(this, "Password should be between 3 and 16 characters long", Toast.LENGTH_SHORT).show()
            return false
        }

        // 3. A valid password should not contain any special characters or empty spaces
        val regex = Regex("[^A-Za-z0-9 ]") // matches any character that is not alphanumeric or space
        if (regex.containsMatchIn(password)) {
            Toast.makeText(this, "Password should not contain any special characters or empty spaces ", Toast.LENGTH_SHORT).show()
            return false
        }

        // TODO: add more rules

        return true
    }

    private fun validateUsername(username : String): Boolean {
        // 1. A valid username should not be empty
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Username must not be empty", Toast.LENGTH_SHORT).show()
            return false
        }

        // 2. A valid username should be between 3 and 16 characters long
        if (username.length < 3 || username.length > 16) {
            Toast.makeText(this, "Username should be between 3 and 16 characters long", Toast.LENGTH_SHORT).show()
            return false
        }

        // 3. A valid username should not contain any special characters or empty spaces
        val regex = Regex("[^A-Za-z0-9 ]") // matches any character that is not alphanumeric or space
        if (regex.containsMatchIn(username)) {
            Toast.makeText(this, "Username should not contain any special characters or empty spaces ", Toast.LENGTH_SHORT).show()
            return false
        }

        // TODO: add more rules

        return true
    }

    private fun updateDB(username : String, password : String): Deferred<String> = CoroutineScope(Dispatchers.IO).async {
        // Construct the complete URL with query parameters
        val baseUrl = "https://cs501andriodsquad.com:4567/api/user/create" // TODO: Fill in the server base url
        val queryParam1 = "username=$username"
        val queryParam2 = "password=$password"
        val url = URL("$baseUrl?$queryParam1&$queryParam2")

        // Send HTTP POST request to fetch user password
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.connectTimeout = 5000     // optional timeout value in milliseconds

        val responseCode = connection.responseCode
        val response:String = if(responseCode == HttpURLConnection.HTTP_OK){
            "New user added successfully."
        }else if(responseCode == HttpURLConnection.HTTP_BAD_REQUEST){
            "Something went wrong, please check the input. "
        }else{
            "Unexpected HTTP response: ${connection.responseCode}"
        }

        response


        // Read the response from the request
//        val inputStream = connection.inputStream
//        val reader = BufferedReader(InputStreamReader(inputStream))
//        val response = StringBuffer()
//
//        var line: String? = reader.readLine()
//        while (line != null) {
//            response.append(line)
//            line = reader.readLine()
//        }
//
//        // Clean up resources
//        reader.close()
//        inputStream.close()
//        connection.disconnect()
//
//        response.toString()
    }


    private fun queryDB(username : String) : Deferred<String> = CoroutineScope(Dispatchers.IO).async {
        // Construct the complete URL with query parameters
        val baseUrl = "https://cs501andriodsquad.com:4567/api/user/password" // TODO: Fill in the server base url
        val queryParam = "username=$username"
        val url = URL("$baseUrl?$queryParam")

        // Send HTTP GET request to fetch user password
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 5000     // optional timeout value in milliseconds

        val responseCode = connection.responseCode
        if(responseCode == HttpURLConnection.HTTP_NOT_FOUND){
            return@async "User not found."
        }else if (responseCode != HttpURLConnection.HTTP_OK) {
            // Handle other non-success HTTP response codes
            return@async "Error: $responseCode"
        }

        // Read the response from the request
        val inputStream = connection.inputStream
        val reader = BufferedReader(InputStreamReader(inputStream))
        val response = StringBuffer()

        var line: String? = reader.readLine()
        while (line != null) {
            response.append(line)
            line = reader.readLine()
        }

        // Clean up resources
        reader.close()
        inputStream.close()
        connection.disconnect()

        response.toString()
    }


}