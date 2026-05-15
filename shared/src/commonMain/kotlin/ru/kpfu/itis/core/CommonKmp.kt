package ru.kpfu.itis.core

import app.cash.sqldelight.db.SqlDriver
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import ru.kpfu.itis.Database
import ru.kpfu.itis.core.config.Configuration
import ru.kpfu.itis.core.di.platformModule
import ru.kpfu.itis.core.di.qualifierModule
import ru.kpfu.itis.core.network.networkModule
import ru.kpfu.itis.feature.auth.data.datasource.UserDataSource


// точка инициализации di
object CommonKmp {

    fun initKoin(
        configuration: Configuration,
        appDeclaration: KoinAppDeclaration = {},
    ) {
        startKoin {
            appDeclaration()
            modules(
                createConfiguration(configuration),
                qualifierModule,
                platformModule,
                networkModule
            )
        }
    }

    private fun createConfiguration(configuration: Configuration) = module {
        single<Configuration> { configuration }

        single <Database> {
            Database(get<SqlDriver>())
        }
        factory{
            UserDataSource(get())
        }
    }
}
