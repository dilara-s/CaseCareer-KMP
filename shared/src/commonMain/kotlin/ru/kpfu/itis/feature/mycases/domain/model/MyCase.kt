package ru.kpfu.itis.feature.mycases.domain.model

data class MyCase(
    val caseId: Int,
    val title: String,
    val companyName: String,
    val submittedAt: String,
    val status: String
)
