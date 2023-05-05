package com.example.a501project.data

import android.app.AlertDialog
import android.util.Log
import com.example.a501project.data.model.LoggedInUser
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
// 此类用于和数据库交互
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication

            // Construct the complete URL with query parameters
            val baseUrl = "<Fill in the server base url>/api/user/password"
            val queryParam = "username=$username"
            val url = URL("$baseUrl?$queryParam")

            // Send HTTP GET request to fetch user password
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000     // optional timeout value in milliseconds

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

            // Parse the response
            if (response.toString().equals("User not found.")) {
                throw Exception("User not found.")
            } else if (response.toString().equals(password)) {
                val user = LoggedInUser(java.util.UUID.randomUUID().toString(), username)
                return Result.Success(user)
            } else {
                throw IOException("Wrong password.")
            }

//            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
//            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}