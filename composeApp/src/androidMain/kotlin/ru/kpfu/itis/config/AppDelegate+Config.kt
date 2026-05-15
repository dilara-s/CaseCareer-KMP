package ru.kpfu.itis.config

import ru.kpfu.itis.AppDelegate
import ru.kpfu.itis.BuildConfig
import ru.kpfu.itis.core.CommonKmp
import ru.kpfu.itis.core.config.Configuration
import org.koin.android.ext.koin.androidContext

internal fun AppDelegate.initCommon() {
    val config = Configuration(
        isDebug = BuildConfig.DEBUG,
        isHttpLoggingEnabled = BuildConfig.DEBUG,
    )
    CommonKmp.initKoin(config) {
        androidContext(applicationContext)
    }
}