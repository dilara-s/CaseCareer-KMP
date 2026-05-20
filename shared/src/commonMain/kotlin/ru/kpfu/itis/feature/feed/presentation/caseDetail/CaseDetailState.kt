package ru.kpfu.itis.feature.feed.presentation.caseDetail

import ru.kpfu.itis.feature.feed.domain.model.CaseDetail

data class CaseDetailState(
    val case: CaseDetail? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)
