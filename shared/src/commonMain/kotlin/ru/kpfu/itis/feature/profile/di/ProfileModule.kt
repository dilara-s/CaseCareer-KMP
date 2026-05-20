package ru.kpfu.itis.feature.profile.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kpfu.itis.feature.profile.data.remote.ProfileApi
import ru.kpfu.itis.feature.profile.data.repository.ProfileRepositoryImpl
import ru.kpfu.itis.feature.profile.domain.repository.ProfileRepository
import ru.kpfu.itis.feature.profile.domain.usecase.GetProfileUseCase
import ru.kpfu.itis.feature.profile.domain.usecase.LogoutUseCase
import ru.kpfu.itis.feature.profile.presentation.ProfileViewModel

val profileModule = module {
    single { ProfileApi(get()) }
    singleOf(::ProfileRepositoryImpl) bind ProfileRepository::class
    factoryOf(::GetProfileUseCase)
    factoryOf(::LogoutUseCase)
    factory { ProfileViewModel(get(), get()) }
}
