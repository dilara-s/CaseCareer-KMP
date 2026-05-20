package ru.kpfu.itis.feature.mycases.domain.usecase

import ru.kpfu.itis.feature.mycases.domain.model.MyCase
import ru.kpfu.itis.feature.mycases.domain.repository.MyCasesRepository

class GetMyCasesUseCase(private val repository: MyCasesRepository) {
    suspend operator fun invoke(): Result<List<MyCase>> = repository.getMyCases()
}
