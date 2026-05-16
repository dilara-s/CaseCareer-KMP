package ru.kpfu.itis.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.kpfu.itis.feature.auth.presentation.AuthViewModel

val viewModelModule = module {
    viewModel {
        AuthViewModel(
            loginUseCase = get(),
            registerUseCase = get()
        )
    }
}