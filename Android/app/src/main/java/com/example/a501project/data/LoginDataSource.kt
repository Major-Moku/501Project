package com.example.a501project.data

import com.example.a501project.data.model.LoggedInUser
import java.io.IOException
import okhttp3.*

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
// 此类用于和数据库交互
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            val client = OkHttpClient()
            val requestBody = FormBody.Builder()
                .add("key1", "value1")
                .build()
            val request = Request.Builder()
                .url("https://your-database-api.com/api/endpoint")
                .post(requestBody)
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    // Handle failure
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        // Handle success, parse response
                    } else {
                        // Handle error, e.g., show error message
                    }
                }
            })

            val usr = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}