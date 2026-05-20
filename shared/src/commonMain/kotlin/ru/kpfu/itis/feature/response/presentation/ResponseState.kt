package ru.kpfu.itis.feature.response.presentation

data class ResponseState(
    val screenMode: ResponseScreenMode = ResponseScreenMode.NdaStep,
    val caseId: Int = 0,
    val caseTitle: String = "",
    val companyName: String = "",
    val isNdaRequired: Boolean = false,
    val isNdaAccepted: Boolean = false,
    val ndaError: String? = null,
    val coverLetter: String = "",
    val coverLetterError: String? = null,
    val solutionLink: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val submittedAt: String? = null,
    val responseStatus: String? = null
)

enum class ResponseScreenMode { NdaStep, FormStep, SuccessStep }
