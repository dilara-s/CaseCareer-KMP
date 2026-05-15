package ru.kpfu.itis.core.config

data class `Configuration`(
    val isHttpLoggingEnabled: Boolean,
    val isDebug: Boolean,
) {

    enum class DeviceType {
        Tablet,
        Phone,
    }
}