package ru.kpfu.itis.feature.auth.data.datasource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import ru.kpfu.itis.Database
import ru.kpfu.itis.feature.auth.domain.model.User

class UserDataSource(
    private val database: Database
) {
   /* suspend fun getUser() = withContext(Dispatchers.IO) {
        database.userQueries.getUser().executeAsList()
    }*/

    fun getUser(): User? {
        return database.userQueries.getUser().executeAsOneOrNull()
            ?.let { User(id = it.id, email = it.email, name = it.name) }
    }

    suspend fun upsertUser(id: Long, email: String, name: String) = withContext(Dispatchers.IO) {
        database.userQueries.upsertUser(id = id, email = email, name = name)
    }

    suspend fun clearUser() = withContext(Dispatchers.IO) {
        database.userQueries.clearUser()
    }
}
