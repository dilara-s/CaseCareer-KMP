package ru.kpfu.itis.feature

import ru.kpfu.itis.feature.auth.di.authModule
import ru.kpfu.itis.feature.feed.di.feedModule

val featureModule = listOf(authModule, feedModule)