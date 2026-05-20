package ru.kpfu.itis.feature

import ru.kpfu.itis.feature.auth.di.authModule
import ru.kpfu.itis.feature.mycases.di.myCasesModule
import ru.kpfu.itis.feature.profile.di.profileModule
import ru.kpfu.itis.feature.response.di.responseModule

val featureModule = listOf(authModule, profileModule, responseModule, myCasesModule)