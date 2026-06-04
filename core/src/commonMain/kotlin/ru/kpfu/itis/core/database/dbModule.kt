package ru.kpfu.itis.core.database

import app.cash.sqldelight.db.SqlDriver
import org.koin.dsl.module
import ru.kpfu.itis.Database

val dbModule =
    module {
        single<Database> {
            Database(get<SqlDriver>())
        }
    }
