package ru.kpfu.itis.feature.mycases.presentation

import ru.kpfu.itis.feature.mycases.domain.model.MyCase

data class MyCasesState(
    val isLoading: Boolean = false,
    val cases: List<MyCase> = emptyList(),
    val error: String? = null
)
