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

object Warframe {
    private val client = HttpClient {
        install(JsonFeature)
    }

    suspend fun isWarframeServerOnline(): Boolean = withContext(Dispatchers.IO) {
        val response: String = client.get("https://api.warframestat.us/pc/alerts"){
            headers {
                append("User-Agent", "PostmanRuntime/7.32.2")
                append("Accept", "*/*")
            }
        }
        val serverStatus = Json.parseToJsonElement(response).jsonArray
        serverStatus?.isEmpty() ?: false
    }
}
