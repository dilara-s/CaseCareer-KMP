package ru.kpfu.itis.feature.auth.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequest(val refresh: String)
