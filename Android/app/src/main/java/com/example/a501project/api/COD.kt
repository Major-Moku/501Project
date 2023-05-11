package com.example.a501project.api

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.features.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object COD {
    private val client = HttpClient {
        install(JsonFeature)
    }

    suspend fun getServerStatuses(): Map<String, Boolean> = withContext(Dispatchers.IO) {
        val response: String = client.get("https://support.activision.com/services/apexrest/web/oshp/landingpage")  // Update with the correct URL
        val serverStatus = Json.parseToJsonElement(response).jsonObject
        val serverStatuses = serverStatus["serverStatuses"]?.jsonArray

        serverStatuses?.associate { status ->
            val statusObject = status.jsonObject
            val gameTitle = statusObject["gameTitle"]?.jsonPrimitive?.content ?: ""
            val isOnline = statusObject["status"]?.jsonPrimitive?.content == "null"
            gameTitle to isOnline
        } ?: emptyMap()
    }
}

