package ru.kpfu.itis.core.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

actual abstract class CommonViewModel actual constructor() {

    actual val viewModelScope = MainScope()

    protected actual open fun onClear() {
    }

    fun clear() {
        viewModelScope.cancel()
    }
}