package ru.kpfu.itis.core.viewmodel

import kotlinx.coroutines.CoroutineScope
import androidx.lifecycle.ViewModel as AndroidXViewModel
import androidx.lifecycle.viewModelScope as androidXViewModelScope

actual abstract class CommonViewModel actual constructor() : AndroidXViewModel() {
    actual val viewModelScope: CoroutineScope = androidXViewModelScope

    protected actual open fun onClear() {
        super.onCleared()
    }
}