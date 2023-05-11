package com.example.a501project.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a501project.NavigationActivity
import com.example.a501project.data.CurrentUser
import com.example.a501project.databinding.ActivityLoginBinding
import com.example.a501project.ui.register.RegisterActivity
import io.ktor.network.sockets.*
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


//class LoginActivity : AppCompatActivity() {
//
//    private lateinit var loginViewModel: LoginViewModel
//    private lateinit var binding: ActivityLoginBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityLoginBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val username = binding.username
//        val password = binding.password
//        val login = binding.login
//        val loading = binding.loading
//
//        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
//            .get(LoginViewModel::class.java)  //获取ViewModel,让ViewModel与此activity绑定
//
//        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
//            val loginState = it ?: return@Observer
//
//            // disable login button unless both username / password is valid
//            login.isEnabled = loginState.isDataValid
//
//            if (loginState.usernameError != null) {
//                username.error = getString(loginState.usernameError)
//            }
//            if (loginState.passwordError != null) {
//                password.error = getString(loginState.passwordError)
//            }
//        })
//
//        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
//            val loginResult = it ?: return@Observer
//
////            updateUiWithUser()
//            loading.visibility = View.GONE
//            if (loginResult.error != null) {
//                showLoginFailed(loginResult.error)
//            }
//            if (loginResult.success != null) {
//                updateUiWithUser(loginResult.success)
//            }
//            setResult(Activity.RESULT_OK)
//
//            //Complete and destroy login activity once successful
//            finish()
//        })
//
//        username.afterTextChanged {
//            loginViewModel.loginDataChanged(
//                username.text.toString(),
//                password.text.toString()
//            )
//        }
//
//        password.apply {
//            afterTextChanged {
//                loginViewModel.loginDataChanged(
//                    username.text.toString(),
//                    password.text.toString()
//                )
//            }
//
//            setOnEditorActionListener { _, actionId, _ ->
//                when (actionId) {
//                    EditorInfo.IME_ACTION_DONE ->
//                        loginViewModel.login(
//                            username.text.toString(),
//                            password.text.toString()
//                        )
//                }
//                false
//            }
//
//            login.setOnClickListener {
//                loading.visibility = View.VISIBLE
//                loginViewModel.login(username.text.toString(), password.text.toString())  // 调用viewmodel的login函数以设置loginresult.value
//            }
//        }
//
//
//        binding.register?.setOnClickListener {
//            val intent = Intent(this, RegisterActivity::class.java)
//            startActivity(intent)
//        }
//
//    }
//
//    private fun updateUiWithUser() {
//        // temporary
//        val intent = Intent(this, NavigationActivity::class.java)
//        startActivity(intent)
//    }
//    private fun updateUiWithUser(model: LoggedInUserView) {
//        val welcome = getString(R.string.welcome)
//        val displayName = model.displayName
//        // TODO : initiate successful logged in experience
//        Toast.makeText(
//            applicationContext,
//            "$welcome $displayName",
//            Toast.LENGTH_LONG
//        ).show()
//        val intent = Intent(this, NavigationActivity::class.java)
//        startActivity(intent)
//    }
//
//    private fun showLoginFailed(@StringRes errorString: Int) {
//        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
//    }
//}
//
///**
// * Extension function to simplify setting an afterTextChanged action to EditText components.
// */
//fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
//    this.addTextChangedListener(object : TextWatcher {
//        override fun afterTextChanged(editable: Editable?) {
//            afterTextChanged.invoke(editable.toString())
//        }
//
//        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//
//        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
//    })
//}


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val textUsername = binding.username
        val textPassword = binding.password
        val buttonLogin = binding.login

        buttonLogin.setOnClickListener {
            val username = textUsername.text.toString()
            val password = textPassword.text.toString()

            // Validate text field value of username
            val isValidUsername = validateUsername(username)
            if (!isValidUsername) return@setOnClickListener

            // Validate text field value of password
            val isValidPassword = validatePassword(password)
            if (!isValidPassword) return@setOnClickListener
//            buttonLogin.isEnabled = true

            // Query the database to see if the user is registered
            val result = queryDB(username)
            CoroutineScope(Dispatchers.Main).launch {
                val response = result.await()
//                println(response)
                if (response.equals("User not found.")){
                    Toast.makeText(this@LoginActivity, "User not found. Please register.", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                if (!response.equals(password)) {
                    Toast.makeText(this@LoginActivity, "Incorrect password. Please try again.", Toast.LENGTH_SHORT).show()
                } else{
                    Toast.makeText(this@LoginActivity, "Logged in successfully", Toast.LENGTH_SHORT).show()
                    //TODO: set the value for CurrentUser.
                    val descriptionResult = queryDescription(username).await()
                    CurrentUser.setCredentials(username,response, descriptionResult)
                    startActivity(Intent(this@LoginActivity, NavigationActivity::class.java))
                }
                // Update UI with response
                // For instance: textView.text = response
            }


//            val isUserExist = isRegistered(username)
//            if (!isUserExist) {
//                Toast.makeText(this, "User not found. Please register.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            // Authenticate the user. See if the password is correct
//            val isPasswordCorrect = isPasswordCorrect(username, password)
//            if (!isPasswordCorrect) return@setOnClickListener
//            // Show a toast message indicating success
//            Toast.makeText(this, "Logged in successfully", Toast.LENGTH_SHORT).show()
//
//
//            // TODO: Start the navigation activity
//            startActivity(Intent(this, NavigationActivity::class.java))
        }


        binding.register?.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

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

    private fun isRegistered(username : String): Boolean {
        val response = queryDB(username)
        if (response.equals("User not found.")) {
            return false
        }
        return true
    }

    private fun isPasswordCorrect(username : String, password: String): Boolean {
        val response = queryDB(username)
        if (!response.equals(password)) {
            Toast.makeText(this, "Incorrect password. Please try again.", Toast.LENGTH_SHORT).show()
            return false;
        }
//        CurrentUser.setCredentials(username, response)
        return true;
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

    private fun queryDescription (username : String) : Deferred<String> = CoroutineScope(Dispatchers.IO).async{
        val baseUrl = "https://cs501andriodsquad.com:4567/api/user/description" // TODO: Fill in the server base url
        val queryParam = "username=$username"
        val url = URL("$baseUrl?$queryParam")

        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 5000
        val responseCode = connection.responseCode

        if(responseCode != HttpURLConnection.HTTP_OK){
            return@async "Error: $responseCode"
        }

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