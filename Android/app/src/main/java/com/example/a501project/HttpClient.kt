import okhttp3.OkHttpClient

object HttpClient {
    val instance: OkHttpClient by lazy {
        OkHttpClient.Builder()
            // add any custom configuration you need here
            .build()
    }
}
