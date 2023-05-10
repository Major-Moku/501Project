package com.example.a501project.api

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.features.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object LostArk {
    private val client = HttpClient {
        install(JsonFeature)
    }

    suspend fun isLostArkServerOnline(): Boolean = withContext(Dispatchers.IO) {
        val response: String = client.get("https://lost-ark-api.vercel.app/server/all")
        val serverStatus = Json.parseToJsonElement(response).jsonObject
        val services = serverStatus["data"]?.jsonObject
        services?.forEach { (key, value) ->
            if (value.jsonPrimitive.content == "❌ Offline") {
                return@withContext false
            }
        }
        true
    } ?: false
}
