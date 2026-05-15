package ru.kpfu.itis.core.di


import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.kpfu.itis.Database

actual val platformModule: Module
    get() = module {
        single<SqlDriver> {
            AndroidSqliteDriver(
                schema = Database.Schema,
                context = get<Context>(),
                name = get(named<QualifierDBName>()),
            )
        }

        single<Settings> {
            SharedPreferencesSettings(
                delegate = get<Context>()
                    .getSharedPreferences(
                        get(named<QualifierSettingName>()),
                        Context.MODE_PRIVATE
                    )
            )
        }
    }
