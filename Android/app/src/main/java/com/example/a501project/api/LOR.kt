package com.example.a501project.api

import com.example.a501project.data.RiotApiKey
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.features.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

object LOR {
    private val client = HttpClient {
        install(JsonFeature)
    }

    suspend fun isLORServerOnline(): Boolean = withContext(Dispatchers.IO) {
        val response: String = client.get("https://americas.api.riotgames.com/lor/status/v1/platform-data?api_key=${RiotApiKey.value}"){
            headers {
                append("User-Agent", "PostmanRuntime/7.32.2")
                append("Accept", "*/*")
            }
        }
        val serverStatus = Json.parseToJsonElement(response).jsonObject
        val services = serverStatus["maintenances"]?.jsonArray
        services?.isEmpty() ?: false
    }
}
