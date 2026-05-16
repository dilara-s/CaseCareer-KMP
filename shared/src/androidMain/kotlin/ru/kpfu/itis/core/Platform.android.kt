package ru.kpfu.itis.core


import android.os.Build
import ru.kpfu.itis.Platform

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

//actual fun getPlatform(): Platform = AndroidPlatform()