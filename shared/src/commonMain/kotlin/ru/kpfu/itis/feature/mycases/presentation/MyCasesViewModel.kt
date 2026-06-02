package ru.kpfu.itis.feature.mycases.presentation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kpfu.itis.core.viewmodel.CommonViewModel
import ru.kpfu.itis.feature.mycases.domain.usecase.GetMyCasesUseCase

class MyCasesViewModel(
    private val getMyCasesUseCase: GetMyCasesUseCase,
) : CommonViewModel() {
    private val _state = MutableStateFlow(MyCasesState())
    val state: StateFlow<MyCasesState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MyCasesEffect>(extraBufferCapacity = 1)
    val effect: SharedFlow<MyCasesEffect> = _effect.asSharedFlow()

    init {
        onEvent(MyCasesEvent.Load)
    }

    fun onEvent(event: MyCasesEvent) {
        when (event) {
            MyCasesEvent.Load, MyCasesEvent.Refresh -> loadCases()
            is MyCasesEvent.OpenCase ->
                viewModelScope.launch {
                    _effect.emit(MyCasesEffect.NavigateToCaseDetail(event.caseId))
                }
        }
    }

    private fun loadCases() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getMyCasesUseCase()
                .onSuccess { cases ->
                    _state.update { it.copy(isLoading = false, cases = cases) }
                }
                .onFailure { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }
}
