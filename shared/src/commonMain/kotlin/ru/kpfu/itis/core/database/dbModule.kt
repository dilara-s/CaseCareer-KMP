package ru.kpfu.itis.core.database

import app.cash.sqldelight.db.SqlDriver
import org.koin.dsl.module
import ru.kpfu.itis.Database
import ru.kpfu.itis.feature.auth.data.datasource.UserDataSource

val dbModule = module {

    single<Database> {
        Database(get<SqlDriver>())
    }

    single {
        UserDataSource(
            database = get()
        )
    }
}