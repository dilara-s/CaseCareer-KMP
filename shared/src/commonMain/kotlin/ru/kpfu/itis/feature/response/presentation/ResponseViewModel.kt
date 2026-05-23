package ru.kpfu.itis.feature.response.presentation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kpfu.itis.core.mock.MockResponseStore
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
            is ResponseEvent.Init -> initForm(event)
            ResponseEvent.ToggleNdaAccepted -> _state.update {
                it.copy(isNdaAccepted = !it.isNdaAccepted, ndaError = null)
            }
            ResponseEvent.ProceedFromNda -> proceedFromNda()
            is ResponseEvent.UpdateCoverLetter -> _state.update {
                it.copy(coverLetter = event.text, coverLetterError = null, error = null)
            }
            is ResponseEvent.UpdateSolutionLink -> _state.update {
                it.copy(solutionLink = event.link, solutionLinkError = null, error = null)
            }
            ResponseEvent.Submit -> submit()
            ResponseEvent.Cancel -> viewModelScope.launch { _effect.emit(ResponseEffect.CloseSheet) }
            ResponseEvent.BackToFeed -> viewModelScope.launch { _effect.emit(ResponseEffect.NavigateToMyCases) }
        }
    }

    private fun initForm(event: ResponseEvent.Init) {
        // Проверяем не откликался ли уже на этот кейс (через мок-стор)
        // Когда подключим реальный API — заменить на проверку через getMyCasesUseCase
        val alreadyResponded = MockResponseStore.getAll().any { it.caseId == event.caseId }

        _state.update {
            it.copy(
                caseId = event.caseId,
                caseTitle = event.caseTitle,
                companyName = event.companyName,
                isNdaRequired = event.ndaRequired,
                totalSteps = if (event.ndaRequired) 3 else 2,
                screenMode = if (event.ndaRequired) ResponseScreenMode.NdaStep else ResponseScreenMode.FormStep,
                alreadyResponded = alreadyResponded
            )
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

        // Проверка двойного отклика
        if (s.alreadyResponded) {
            _state.update { it.copy(error = "Вы уже откликались на этот кейс") }
            return
        }

        // Валидация сопроводительного письма
        if (s.coverLetter.isBlank()) {
            _state.update { it.copy(coverLetterError = "Напишите сопроводительное письмо") }
            return
        }

        // Валидация ссылки на решение — обязательное поле
        val linkError = validateSolutionLink(s.solutionLink)
        if (linkError != null) {
            _state.update { it.copy(solutionLinkError = linkError) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            submitResponseUseCase(s.caseId, s.caseTitle, s.companyName, s.coverLetter, s.solutionLink)
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            screenMode = ResponseScreenMode.SuccessStep,
                            submittedAt = result.submittedAt,
                            responseStatus = result.status,
                            alreadyResponded = true
                        )
                    }
                }
                .onFailure { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }

    // Ссылка обязательна и должна начинаться с http:// или https://
    private fun validateSolutionLink(link: String): String? {
        if (link.isBlank()) return "Введите ссылку на решение"
        if (!link.startsWith("http://") && !link.startsWith("https://")) {
            return "Ссылка должна начинаться с http:// или https://"
        }
        return null
    }
}
