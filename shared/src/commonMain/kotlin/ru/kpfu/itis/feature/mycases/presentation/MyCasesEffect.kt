package ru.kpfu.itis.feature.mycases.presentation

sealed interface MyCasesEffect {
    data class NavigateToCaseDetail(val caseId: Int) : MyCasesEffect
}
