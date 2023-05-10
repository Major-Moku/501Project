package com.example.a501project.ui.profile

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.a501project.data.CurrentUser
import com.example.a501project.databinding.FragmentProfileEditBinding
import com.example.a501project.ui.login.LoginActivity
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ProfileEditFragment: Fragment() {

    private var _binding: FragmentProfileEditBinding? = null
    private val binding
            get() = checkNotNull(_binding){
        "can not access."
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileEditBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.saveButton.setOnClickListener {
            val oldUsername = CurrentUser.username
            val newUserName = binding.newUsername.text.toString()
            val password = binding.newPwd.text.toString()
            val description = binding.newDescrip.text.toString()

            val isValidUsername = validateUsername(newUserName)
            if (!isValidUsername) return@setOnClickListener

            // Validate text field value of password
            val isValidPassword = validatePassword(password)
            if (!isValidPassword) return@setOnClickListener

            val result = updateDbForEdit(oldUsername, newUserName, password, description)
            CoroutineScope(Dispatchers.Main).launch{
                val response = result.await()
                if(response.equals("User updated successfully.")){
                    Toast.makeText(requireContext(),response,Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                else{
                    Toast.makeText(requireContext(), "Something went wrong, please check the input.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateDbForEdit(oldUsername : String, newUserName:String, newPassword : String, description:String): Deferred<String> = CoroutineScope(
        Dispatchers.IO).async{
        val baseUrl = "https://cs501andriodsquad.com:4567/api/user/updateUser" // TODO: Fill in the server base url
        val queryParam1 = "oldUsername=$oldUsername"
        val queryParam2 = "newUsername=$newUserName"
        val queryParam3 = "newPassword=$newPassword"
        val queryParam4 = "description=$description"
        val url = URL("$baseUrl?$queryParam1&$queryParam2&$queryParam3&$queryParam4")

        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "PUT"
        connection.connectTimeout = 5000

        val responseCode = connection.responseCode
        val response:String = if(responseCode == HttpURLConnection.HTTP_OK){
            "User updated successfully."
        }else if(responseCode == HttpURLConnection.HTTP_BAD_REQUEST){
            "Something went wrong, please check the input. "
        }else{
            "Unexpected HTTP response: ${connection.responseCode}"
        }
        response


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
//        reader.close()
//        inputStream.close()
//        connection.disconnect()
//
//        return response.toString()
    }

    private fun validatePassword(password : String): Boolean {
        // 1. A valid password should not be empty
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(requireContext(), "Password must not be empty", Toast.LENGTH_SHORT).show()
            return false
        }

        // 2. A valid password should be between 3 and 16 characters long
        if (password.length < 3 || password.length > 16) {
            Toast.makeText(requireContext(), "Password should be between 3 and 16 characters long", Toast.LENGTH_SHORT).show()
            return false
        }

        // 3. A valid password should not contain any special characters or empty spaces
        val regex = Regex("[^A-Za-z0-9 ]") // matches any character that is not alphanumeric or space
        if (regex.containsMatchIn(password)) {
            Toast.makeText(requireContext(), "Password should not contain any special characters or empty spaces ", Toast.LENGTH_SHORT).show()
            return false
        }

        // TODO: add more rules

        return true
    }

    private fun validateUsername(username : String): Boolean {
        // 1. A valid username should not be empty
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(requireContext(), "Username must not be empty", Toast.LENGTH_SHORT).show()
            return false
        }

        // 2. A valid username should be between 3 and 16 characters long
        if (username.length < 3 || username.length > 16) {
            Toast.makeText(requireContext(), "Username should be between 3 and 16 characters long", Toast.LENGTH_SHORT).show()
            return false
        }

        // 3. A valid username should not contain any special characters or empty spaces
        val regex = Regex("[^A-Za-z0-9 ]") // matches any character that is not alphanumeric or space
        if (regex.containsMatchIn(username)) {
            Toast.makeText(requireContext(), "Username should not contain any special characters or empty spaces ", Toast.LENGTH_SHORT).show()
            return false
        }

        // TODO: add more rules

        return true
    }

}