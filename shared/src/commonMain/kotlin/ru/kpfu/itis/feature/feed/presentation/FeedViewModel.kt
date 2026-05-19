package ru.kpfu.itis.feature.feed.presentation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kpfu.itis.core.viewmodel.CommonViewModel
import ru.kpfu.itis.feature.feed.domain.usecase.GetCasesUseCase
import kotlin.onFailure

class FeedViewModel(
    private val getCasesUseCase: GetCasesUseCase
) : CommonViewModel() {

    private val _state = MutableStateFlow(FeedState())
    val state: StateFlow<FeedState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<FeedEffect>(extraBufferCapacity = 1)
    val effect: SharedFlow<FeedEffect> = _effect.asSharedFlow()

    init {
        loadCases(page = 1, isInitial = true)
    }

    fun onEvent(event: FeedEvent) {
        when (event) {
            is FeedEvent.LoadNextPage -> {
                val s = _state.value
                if (!s.isLoadingMore && !s.isLoading && s.hasMore) {
                    loadCases(page = s.currentPage + 1)
                }
            }
            is FeedEvent.Refresh -> {
                if (!_state.value.isRefreshing) {
                    loadCases(page = 1, isRefresh = true)
                }
            }
            is FeedEvent.CaseClicked -> {
                _effect.tryEmit(FeedEffect.NavigateToCaseDetail(event.caseId))
            }
        }
    }

    private fun loadCases(
        page: Int,
        isInitial: Boolean = false,
        isRefresh: Boolean = false
    ) {
        viewModelScope.launch {
            _state.update {
                when {
                    isInitial -> it.copy(isLoading = true, error = null)
                    isRefresh -> it.copy(isRefreshing = true, error = null)
                    else      -> it.copy(isLoadingMore = true)
                }
            }

            println("FEED_LOG: loadCases page=$page isInitial=$isInitial isRefresh=$isRefresh")

            getCasesUseCase(page)
                .onSuccess { casesPage ->
                    println("FEED_LOG: success, got ${casesPage.cases.size} cases, hasMore=${casesPage.hasNextPage}")
                    _state.update {
                        it.copy(
                            cases = if (isRefresh || isInitial) casesPage.cases
                            else it.cases + casesPage.cases,
                            isLoading = false,
                            isLoadingMore = false,
                            isRefreshing = false,
                            currentPage = page,
                            hasMore = casesPage.hasNextPage,
                            totalCount = casesPage.totalCount,
                            error = null
                        )
                    }
                }
                .onFailure { e ->
                    println("FEED_LOG: error — ${e.message}")
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            isRefreshing = false,
                            error = e.message
                        )
                    }
                    _effect.tryEmit(FeedEffect.ShowError(e.message ?: "Ошибка загрузки"))
                }
        }
    }
}