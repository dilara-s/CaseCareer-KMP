package ru.kpfu.itis.feature.response.presentation

sealed class ResponseEvent {
    data class Init(
        val caseId: Int,
        val caseTitle: String,
        val companyName: String,
        val ndaRequired: Boolean
    ) : ResponseEvent()

    data object ToggleNdaAccepted : ResponseEvent()
    data object ProceedFromNda : ResponseEvent()
    data class UpdateCoverLetter(val text: String) : ResponseEvent()
    data class UpdateSolutionLink(val link: String) : ResponseEvent()
    data object Submit : ResponseEvent()
    data object Cancel : ResponseEvent()
    data object BackToFeed : ResponseEvent()
}
