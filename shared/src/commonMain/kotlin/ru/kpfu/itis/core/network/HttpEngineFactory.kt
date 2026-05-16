package ru.kpfu.itis.core.network

import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import ru.kpfu.itis.core.config.Configuration


expect open class HttpEngineFactory() {

    fun createEngine(configuration: Configuration): HttpClientEngineFactory<HttpClientEngineConfig>
}

