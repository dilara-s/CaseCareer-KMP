package ru.kpfu.itis.feature.auth.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    @SerialName("full_name") val fullName: String,
    val email: String,
    val password: String,
    @SerialName("confirm_password") val confirmPassword: String,
    val phone: String = "",
    @SerialName("contact_info") val contactInfo: String = "",
    @SerialName("portfolio_link") val portfolioLink: String = "",
    val skills: String = ""
)