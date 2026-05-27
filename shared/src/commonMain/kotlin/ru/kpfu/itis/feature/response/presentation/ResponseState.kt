package ru.kpfu.itis.feature.response.presentation

data class ResponseState(
    val screenMode: ResponseScreenMode = ResponseScreenMode.NdaStep,
    val caseId: Int = 0,
    val caseTitle: String = "",
    val companyName: String = "",
    val isNdaRequired: Boolean = false,
    // totalSteps = 3 если NDA нужно, 2 если нет — для прогресс-бара в UI
    val totalSteps: Int = 3,
    val isNdaAccepted: Boolean = false,
    val ndaError: String? = null,
    val coverLetter: String = "",
    val coverLetterError: String? = null,
    val solutionLink: String = "",
    val solutionLinkError: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val submittedAt: String? = null,
    val responseStatus: String? = null,
    val alreadyResponded: Boolean = false
)

enum class ResponseScreenMode { NdaStep, FormStep, SuccessStep }
