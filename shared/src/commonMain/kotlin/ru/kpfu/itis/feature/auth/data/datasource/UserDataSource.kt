/*
package ru.kpfu.itis.feature.auth.data.datasource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import ru.kpfu.itis.Database

class UserDataSource(
    private val database: Database
) {

    suspend fun getUser() = withContext(Dispatchers.IO) {
        database.userQueries.getUser()
    }

    suspend fun addUser() {
        database.userQueries.upsertUser(id = 1, email = "tarkv@gmail.com", name = "Ksenia")
    }
}*/
