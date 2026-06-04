package ru.kpfu.itis.feature.auth.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kpfu.itis.feature.auth.data.datasource.UserDataSource
import ru.kpfu.itis.feature.auth.data.remote.AuthApi
import ru.kpfu.itis.feature.auth.data.repository.AuthRepositoryImpl
import ru.kpfu.itis.feature.auth.domain.repository.AuthRepository
import ru.kpfu.itis.feature.auth.domain.usecase.LoginUseCase
import ru.kpfu.itis.feature.auth.domain.usecase.RegisterUseCase
import ru.kpfu.itis.feature.auth.presentation.AuthViewModel

val authModule =
    module {
        single { UserDataSource(database = get()) }
        single { AuthApi(get()) }
        singleOf(::AuthRepositoryImpl) bind AuthRepository::class
        factoryOf(::LoginUseCase)
        factoryOf(::RegisterUseCase)
        factory {
            AuthViewModel(
                loginUseCase = get(),
                registerUseCase = get(),
            )
        }
    }
