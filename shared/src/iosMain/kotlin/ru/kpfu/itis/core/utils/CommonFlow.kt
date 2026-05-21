package ru.kpfu.itis.core.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

// Обёртка Flow для сбора из Swift — возвращает Cancelable который можно отменить в deinit
class CommonFlow<T>(private val flow: Flow<T>) {
    fun watch(onEach: (T) -> Unit): Cancelable {
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
        scope.launch { flow.collect { onEach(it) } }
        return Cancelable { scope.cancel() }
    }
}

class Cancelable(private val onCancel: () -> Unit) {
    fun cancel() = onCancel()
}

fun <T> Flow<T>.asCommonFlow() = CommonFlow(this)
