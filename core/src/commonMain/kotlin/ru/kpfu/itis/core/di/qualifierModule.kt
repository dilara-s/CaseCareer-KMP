package ru.kpfu.itis.core.di

import org.koin.core.qualifier.named
import org.koin.dsl.module

internal object QualifierDBName

internal object QualifierSettingName

val qualifierModule =
    module {
        factory<String>(named<QualifierDBName>()) {
            "user.db"
        }
        factory<String>(named<QualifierSettingName>()) {
            "settings"
        }
    }
