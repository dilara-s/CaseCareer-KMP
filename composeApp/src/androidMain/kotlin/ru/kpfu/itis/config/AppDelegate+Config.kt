package ru.kpfu.itis.config

import ru.kpfu.itis.AppDelegate
import ru.kpfu.itis.BuildConfig
import ru.kpfu.itis.core.CommonKmp
import ru.kpfu.itis.core.config.Configuration
import org.koin.android.ext.koin.androidContext
import ru.kpfu.itis.di.viewModelModule

internal fun AppDelegate.initCommon() {
    val config = Configuration(
        isDebug = BuildConfig.DEBUG,
        isHttpLoggingEnabled = BuildConfig.DEBUG,
    )
    CommonKmp.initKoin(config) {
        androidContext(applicationContext)
        modules(viewModelModule)   // ← добавляем Android-специфичные viewmodel
    }
}