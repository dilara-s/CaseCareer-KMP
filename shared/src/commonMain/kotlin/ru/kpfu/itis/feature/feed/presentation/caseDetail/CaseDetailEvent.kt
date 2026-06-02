package ru.kpfu.itis.feature.feed.presentation.caseDetail

sealed class CaseDetailEvent {
    object Apply : CaseDetailEvent()

    object Back : CaseDetailEvent()

    object Retry : CaseDetailEvent()
}
