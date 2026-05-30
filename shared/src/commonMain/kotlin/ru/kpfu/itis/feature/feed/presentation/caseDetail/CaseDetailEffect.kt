package ru.kpfu.itis.feature.feed.presentation.caseDetail

sealed class CaseDetailEffect {
    object NavigateBack : CaseDetailEffect()
    data class NavigateToNdaStep(
        val caseId: Long,
        val caseTitle: String,
        val companyName: String
    ) : CaseDetailEffect()
    data class NavigateToApplyStep(
        val caseId: Long,
        val caseTitle: String,
        val companyName: String
    ) : CaseDetailEffect()
    data class ShowError(val message: String) : CaseDetailEffect()
}
