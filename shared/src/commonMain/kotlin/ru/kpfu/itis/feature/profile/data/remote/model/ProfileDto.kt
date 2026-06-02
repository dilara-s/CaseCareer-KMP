package ru.kpfu.itis.feature.profile.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val id: Int,
    val email: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("role_type") val roleType: String,
    val phone: String? = null,
    val skills: String? = null,
    @SerialName("contact_info") val contactInfo: String? = null,
    @SerialName("portfolio_link") val portfolioLink: String? = null,
    val rating: String? = null,
    @SerialName("created_at") val createdAt: String,
)
