package com.example.a501project.api

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.features.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object Dota2 {
    private val client = HttpClient {
        install(JsonFeature)
    }

    suspend fun isDota2ServerOnline(): Boolean = withContext(Dispatchers.IO) {
        val response: String = client.get("https://api.steampowered.com/IGCVersion_570/GetServerVersion/v1/?")
        val serverStatus = Json.parseToJsonElement(response).jsonObject
        val services = serverStatus["result"]?.jsonObject
        services?.get("success")?.jsonPrimitive?.content == "true"
    } ?: false
}
