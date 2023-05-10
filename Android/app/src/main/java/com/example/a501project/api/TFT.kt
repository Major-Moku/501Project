package com.example.a501project.api

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.features.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

object TFT {
    private val client = HttpClient {
        install(JsonFeature)
    }

    suspend fun isTFTServerOnline(): Boolean = withContext(Dispatchers.IO) {
        val response: String = client.get("https://na1.api.riotgames.com/tft/status/v1/platform-data?api_key=RGAPI-50cb78b7-21f1-4539-9295-587b9fec524e"){
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
