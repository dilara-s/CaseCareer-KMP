package ru.kpfu.itis.feature.response.presentation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kpfu.itis.core.viewmodel.CommonViewModel
import ru.kpfu.itis.feature.response.domain.usecase.SubmitResponseUseCase

class ResponseViewModel(
    private val submitResponseUseCase: SubmitResponseUseCase
) : CommonViewModel() {

    private val _state = MutableStateFlow(ResponseState())
    val state: StateFlow<ResponseState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ResponseEffect>(extraBufferCapacity = 1)
    val effect: SharedFlow<ResponseEffect> = _effect.asSharedFlow()

    fun onEvent(event: ResponseEvent) {
        when (event) {
            is ResponseEvent.Init -> _state.update {
                it.copy(
                    caseId = event.caseId,
                    caseTitle = event.caseTitle,
                    companyName = event.companyName,
                    isNdaRequired = event.ndaRequired,
                    screenMode = if (event.ndaRequired) ResponseScreenMode.NdaStep else ResponseScreenMode.FormStep
                )
            }
            ResponseEvent.ToggleNdaAccepted -> _state.update {
                it.copy(isNdaAccepted = !it.isNdaAccepted, ndaError = null)
            }
            ResponseEvent.ProceedFromNda -> proceedFromNda()
            is ResponseEvent.UpdateCoverLetter -> _state.update {
                it.copy(coverLetter = event.text, coverLetterError = null, error = null)
            }
            is ResponseEvent.UpdateSolutionLink -> _state.update {
                it.copy(solutionLink = event.link, error = null)
            }
            ResponseEvent.Submit -> submit()
            ResponseEvent.Cancel -> viewModelScope.launch { _effect.emit(ResponseEffect.CloseSheet) }
            ResponseEvent.BackToFeed -> viewModelScope.launch { _effect.emit(ResponseEffect.CloseSheet) }
        }
    }

    private fun proceedFromNda() {
        if (!_state.value.isNdaAccepted) {
            _state.update { it.copy(ndaError = "Подтвердите согласие с NDA для продолжения") }
            return
        }
        _state.update { it.copy(screenMode = ResponseScreenMode.FormStep, ndaError = null) }
    }

    private fun submit() {
        val s = _state.value
        if (s.coverLetter.isBlank()) {
            _state.update { it.copy(coverLetterError = "Напишите сопроводительное письмо") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            submitResponseUseCase(s.caseId, s.coverLetter, s.solutionLink)
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            screenMode = ResponseScreenMode.SuccessStep,
                            submittedAt = result.submittedAt,
                            responseStatus = result.status
                        )
                    }
                }
                .onFailure { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }
}
