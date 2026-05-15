package ru.kpfu.itis.core.viewmodel

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<State : Any, Effect, Event>(
    initState: State
) : CommonViewModel() {

    private val _state = MutableStateFlow(initState)
    private val _effect = MutableSharedFlow<Effect>(extraBufferCapacity = 1)

    val state: StateFlow<State> = _state.asStateFlow()
    val effect: SharedFlow<Effect> = _effect.asSharedFlow()

    protected fun updateState(update: State.() -> State) {
        _state.update { it.update() }
    }

    protected fun emitEffect(eff: Effect) {
        _effect.tryEmit(eff)
    }

    abstract fun onEvent(event: Event)
}