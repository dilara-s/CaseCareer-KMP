import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import ru.kpfu.itis.core.network.HttpEngineFactory
import ru.kpfu.itis.core.network.TokenStorage
import ru.kpfu.itis.core.network.installAuthPlugin


val networkModule = module {
    single { HttpEngineFactory() }

    single<Json> {
        Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
    }

    single { TokenStorage(get()) }

    single {
        buildHttpClient(
            engine = get<HttpEngineFactory>().createEngine(get()),
            json = get(),
            tokenStorage = get()
        )
    }
}

private const val BASE_URL = "/api/v1"   // ← замени на реальный

private fun buildHttpClient(
    engine: HttpClientEngineFactory<HttpClientEngineConfig>,
    json: Json,
    tokenStorage: TokenStorage
) = HttpClient(engine) {

    install(Logging) {
        logger = Logger.SIMPLE
        level = LogLevel.BODY
    }

    install(ContentNegotiation) {
        json(json)
    }

    install(HttpTimeout) {
        connectTimeoutMillis = 5000
        requestTimeoutMillis = 10000
        socketTimeoutMillis = 10000
    }

    defaultRequest {
        url {
            host = BASE_URL
            protocol = URLProtocol.HTTPS
        }
    }

}.also { client ->
    client.installAuthPlugin(tokenStorage)
}