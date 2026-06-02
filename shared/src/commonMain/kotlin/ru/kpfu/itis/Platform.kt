package ru.kpfu.itis

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
