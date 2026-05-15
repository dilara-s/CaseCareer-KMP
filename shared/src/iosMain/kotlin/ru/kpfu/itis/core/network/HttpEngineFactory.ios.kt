package ru.kpfu.itis.core.network

import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin
import ru.kpfu.itis.core.config.Configuration

actual open class HttpEngineFactory actual constructor() {
    actual fun createEngine(configuration: Configuration): HttpClientEngineFactory<HttpClientEngineConfig> =
        Darwin
}