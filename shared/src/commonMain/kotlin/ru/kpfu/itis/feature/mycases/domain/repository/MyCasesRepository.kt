package ru.kpfu.itis.feature.mycases.domain.repository

import ru.kpfu.itis.feature.mycases.domain.model.MyCase

interface MyCasesRepository {
    suspend fun getMyCases(): Result<List<MyCase>>
}
