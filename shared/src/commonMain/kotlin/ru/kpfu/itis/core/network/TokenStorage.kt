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

    // Profile fields saved at registration time
    var profileSkills: String?
        get() = settings.getStringOrNull(KEY_SKILLS)
        set(value) = if (value != null) settings.putString(KEY_SKILLS, value) else settings.remove(KEY_SKILLS)

    var profileContactInfo: String?
        get() = settings.getStringOrNull(KEY_CONTACT)
        set(value) = if (value != null) settings.putString(KEY_CONTACT, value) else settings.remove(KEY_CONTACT)

    var profilePortfolioLink: String?
        get() = settings.getStringOrNull(KEY_PORTFOLIO)
        set(value) = if (value != null) settings.putString(KEY_PORTFOLIO, value) else settings.remove(KEY_PORTFOLIO)

    var profilePhone: String?
        get() = settings.getStringOrNull(KEY_PHONE)
        set(value) = if (value != null) settings.putString(KEY_PHONE, value) else settings.remove(KEY_PHONE)

    fun clearProfile() {
        settings.remove(KEY_SKILLS)
        settings.remove(KEY_CONTACT)
        settings.remove(KEY_PORTFOLIO)
        settings.remove(KEY_PHONE)
    }

    companion object {
        private const val KEY_ACCESS = "access_token"
        private const val KEY_REFRESH = "refresh_token"
        private const val KEY_SKILLS = "profile_skills"
        private const val KEY_CONTACT = "profile_contact_info"
        private const val KEY_PORTFOLIO = "profile_portfolio_link"
        private const val KEY_PHONE = "profile_phone"
    }
}