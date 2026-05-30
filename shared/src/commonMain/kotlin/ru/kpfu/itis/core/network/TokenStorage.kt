package ru.kpfu.itis.core.network

import com.russhwolf.settings.Settings

class TokenStorage(private val settings: Settings) {

    var accessToken: String?
        get() = settings.getStringOrNull(KEY_ACCESS)
        set(value) = if (value != null) settings.putString(KEY_ACCESS, value)
        else settings.remove(KEY_ACCESS)

    var refreshToken: String?
        get() = settings.getStringOrNull(KEY_REFRESH)
        set(value) = if (value != null) settings.putString(KEY_REFRESH, value)
        else settings.remove(KEY_REFRESH)

    fun saveTokens(access: String, refresh: String) {
        accessToken = access
        refreshToken = refresh
    }

    fun clearTokens() {
        settings.remove(KEY_ACCESS)
        settings.remove(KEY_REFRESH)
    }

    fun isLoggedIn(): Boolean = accessToken != null

    companion object {
        private const val KEY_ACCESS = "access_token"
        private const val KEY_REFRESH = "refresh_token"
    }
}