package ru.kpfu.itis.core.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.kpfu.itis.Database

actual val platformModule: Module =
    module {
        single<SqlDriver> {
            NativeSqliteDriver(
                schema = Database.Schema,
                name = get(named<QualifierDBName>()),
            )
        }

        single<Settings> {
            NSUserDefaultsSettings.Factory()
                .create(name = get(named<QualifierSettingName>()))
        }
    }
