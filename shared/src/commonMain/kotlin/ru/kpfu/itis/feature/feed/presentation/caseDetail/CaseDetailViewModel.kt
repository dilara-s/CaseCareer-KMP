package ru.kpfu.itis.feature.feed.presentation.caseDetail

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kpfu.itis.core.viewmodel.CommonViewModel
import ru.kpfu.itis.feature.feed.domain.usecase.GetCaseDetailUseCase
import kotlin.onFailure

class CaseDetailViewModel(
    private val getCaseDetailUseCase: GetCaseDetailUseCase,
    private val caseId: Long
) : CommonViewModel() {

    private val _state = MutableStateFlow(CaseDetailState())
    val state: StateFlow<CaseDetailState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CaseDetailEffect>(extraBufferCapacity = 1)
    val effect: SharedFlow<CaseDetailEffect> = _effect.asSharedFlow()

    init {
        loadCase()
    }

    fun onEvent(event: CaseDetailEvent) {
        when (event) {
            is CaseDetailEvent.Back -> _effect.tryEmit(CaseDetailEffect.NavigateBack)
            is CaseDetailEvent.Retry -> loadCase()
            is CaseDetailEvent.Apply -> {
                val case = _state.value.case ?: return
                if (case.ndaRequired) {
                    _effect.tryEmit(CaseDetailEffect.NavigateToNdaStep(case.id, case.title, case.companyName))
                } else {
                    _effect.tryEmit(CaseDetailEffect.NavigateToApplyStep(case.id, case.title, case.companyName))
                }
            }
        }
    }

    private fun loadCase() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getCaseDetailUseCase(caseId)
                .onSuccess { case ->
                    _state.update { it.copy(case = case, isLoading = false) }
                }
                .onFailure { e ->
                    _state.update { it.copy(isLoading = false, error = e.message ?: "Ошибка загрузки") }
                    _effect.tryEmit(CaseDetailEffect.ShowError(e.message ?: "Ошибка загрузки"))
                }
        }
    }
}
