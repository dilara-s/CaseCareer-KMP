package ru.kpfu.itis.feature.response.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kpfu.itis.feature.response.data.remote.ResponseApi
import ru.kpfu.itis.feature.response.data.repository.ResponseRepositoryImpl
import ru.kpfu.itis.feature.response.domain.repository.ResponseRepository
import ru.kpfu.itis.feature.response.domain.usecase.SubmitResponseUseCase
import ru.kpfu.itis.feature.response.presentation.ResponseViewModel

val responseModule =
    module {
        single { ResponseApi(get()) }
        singleOf(::ResponseRepositoryImpl) bind ResponseRepository::class
        factoryOf(::SubmitResponseUseCase)
        factory { ResponseViewModel(get()) }
    }
