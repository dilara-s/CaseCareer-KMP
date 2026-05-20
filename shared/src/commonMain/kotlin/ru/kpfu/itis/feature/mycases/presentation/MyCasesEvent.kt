package ru.kpfu.itis.feature.mycases.presentation

sealed class MyCasesEvent {
    data object Load : MyCasesEvent()
    data object Refresh : MyCasesEvent()
    data class OpenCase(val caseId: Int) : MyCasesEvent()
}
