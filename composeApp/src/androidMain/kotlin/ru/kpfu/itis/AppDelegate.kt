package ru.kpfu.itis

import android.app.Application
import ru.kpfu.itis.config.initCommon

class AppDelegate : Application() {
    override fun onCreate() {
        super.onCreate()
        initCommon()
    }
}
