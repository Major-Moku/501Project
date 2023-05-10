import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.features.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object Steam {
    private val client = HttpClient {
        install(JsonFeature)
    }

    suspend fun isSteamServerOnline(): Boolean = withContext(Dispatchers.IO) {
        val response: String = client.get("https://api.steampowered.com/ICSGOServers_730/GetGameServersStatus/v1/?key=26DBAC27679399DD378D1B4EFBE438E0")
        val serverStatus = Json.parseToJsonElement(response).jsonObject
        val services = serverStatus["result"]?.jsonObject?.get("services")?.jsonObject
        services?.get("SteamCommunity")?.jsonPrimitive?.content == "normal"
    } ?: false
}
