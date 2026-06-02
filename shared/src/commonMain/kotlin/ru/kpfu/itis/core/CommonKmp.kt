package ru.kpfu.itis.core

import networkModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import ru.kpfu.itis.core.config.Configuration
import ru.kpfu.itis.core.database.dbModule
import ru.kpfu.itis.core.di.platformModule
import ru.kpfu.itis.core.di.qualifierModule
import ru.kpfu.itis.feature.featureModule

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
                networkModule,
                dbModule,
                *featureModule.toTypedArray(),
            )
        }
    }

    private fun createConfiguration(configuration: Configuration) =
        module {
            single<Configuration> { configuration }
        }
}
