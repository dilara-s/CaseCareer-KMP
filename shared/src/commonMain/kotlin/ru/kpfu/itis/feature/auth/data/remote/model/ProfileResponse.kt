package ru.kpfu.itis.feature.auth.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val id: Long,
    val email: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("role_type") val roleType: String,
    val skills: String,
    @SerialName("contact_info") val contactInfo: String,
    @SerialName("portfolio_link") val portfolioLink: String,
    val rating: String,
    @SerialName("created_at") val createdAt: String
)