package ru.kpfu.itis.core.network

import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.config
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.engine.okhttp.OkHttpEngine
import okhttp3.logging.HttpLoggingInterceptor
import ru.kpfu.itis.core.config.Configuration
import timber.log.Timber

actual open class HttpEngineFactory actual constructor() {
    actual fun createEngine(
        configuration: Configuration
    ): HttpClientEngineFactory<HttpClientEngineConfig> = OkHttp.config {
        config {
            retryOnConnectionFailure(true)
        }
        if (configuration.isDebug) {
            addInterceptor(
                HttpLoggingInterceptor { Timber.tag("Network").d(it) }.apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                },
            )
        }
    }
}