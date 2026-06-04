package ru.kpfu.itis.core.viewmodel

import kotlinx.coroutines.CoroutineScope

expect abstract class CommonViewModel() {
    val viewModelScope: CoroutineScope

    protected open fun onClear()
}
