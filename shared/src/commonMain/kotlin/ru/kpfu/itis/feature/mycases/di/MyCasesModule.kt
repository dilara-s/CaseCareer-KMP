package ru.kpfu.itis.feature.mycases.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kpfu.itis.feature.mycases.data.remote.MyCasesApi
import ru.kpfu.itis.feature.mycases.data.repository.MyCasesRepositoryImpl
import ru.kpfu.itis.feature.mycases.domain.repository.MyCasesRepository
import ru.kpfu.itis.feature.mycases.domain.usecase.GetMyCasesUseCase
import ru.kpfu.itis.feature.mycases.presentation.MyCasesViewModel

val myCasesModule =
    module {
        single { MyCasesApi(get()) }
        singleOf(::MyCasesRepositoryImpl) bind MyCasesRepository::class
        factoryOf(::GetMyCasesUseCase)
        factory { MyCasesViewModel(get()) }
    }
