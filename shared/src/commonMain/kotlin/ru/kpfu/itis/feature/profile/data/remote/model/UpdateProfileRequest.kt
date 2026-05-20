package ru.kpfu.itis.feature.profile.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    @SerialName("full_name") val fullName: String? = null,
    val skills: String? = null,
    @SerialName("contact_info") val contactInfo: String? = null,
    @SerialName("portfolio_link") val portfolioLink: String? = null,
    val phone: String? = null
)
