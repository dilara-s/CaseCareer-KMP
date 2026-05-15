package ru.kpfu.itis.feature.auth.data.datasource

import com.russhwolf.settings.Settings
import com.russhwolf.settings.string
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import ru.kpfu.itis.Database

class UserDataSource(
    private val database: Database,
    private val settings: Settings
) {

    val test: String? by settings.string(key = "", defaultValue = "") //здесь можно хранить shared pref, переведя получанный json в строку

    suspend fun getUser() = withContext(Dispatchers.IO) {
        database.userQueries.getUser().executeAsList()
    }

    suspend fun addUser() = withContext(Dispatchers.IO) {
        database.userQueries.upsertUser(id = 1, email = "tarkv@gmail.com", name = "Ksenia")
    }

    suspend fun upsertUser(id: Long, email: String, name: String) = withContext(Dispatchers.IO) {
        database.userQueries.upsertUser(id = id, email = email, name = name)
    }

    suspend fun clearUser() = withContext(Dispatchers.IO) {
        database.userQueries.clearUser()
    }
}
