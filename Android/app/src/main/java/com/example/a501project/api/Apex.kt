import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.features.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object Apex {
    private val client = HttpClient {
        install(JsonFeature)
    }

    suspend fun isApexServerOnline(): Boolean = withContext(Dispatchers.IO) {
        val response: String = client.get("https://api.mozambiquehe.re/servers?auth=895435ce61bf50ecb718797fe63447fa") {
            headers {
                append("User-Agent", "PostmanRuntime/7.32.2")
                append("Accept", "*/*")
            }
        }
        val serverStatus = Json.parseToJsonElement(response).jsonObject
        val services = serverStatus["ApexOauth_Crossplay"]?.jsonObject?.get("US-East")?.jsonObject
        services?.get("Status")?.jsonPrimitive?.content != "DOWN"
    } ?: false

}
