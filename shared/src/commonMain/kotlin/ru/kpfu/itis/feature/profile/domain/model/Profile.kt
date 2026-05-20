package ru.kpfu.itis.feature.profile.domain.model

data class Profile(
    val id: Int,
    val email: String,
    val fullName: String,
    val roleType: String,
    val skills: String,
    val contactInfo: String,
    val portfolioLink: String,
    val phone: String,
    val rating: String,
    val createdAt: String
)
