package ru.kpfu.itis.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.kpfu.itis.feature.auth.presentation.AuthViewModel
import ru.kpfu.itis.feature.feed.presentation.caseDetail.CaseDetailViewModel
import ru.kpfu.itis.feature.feed.presentation.feed.FeedViewModel
import ru.kpfu.itis.feature.mycases.presentation.MyCasesViewModel
import ru.kpfu.itis.feature.profile.presentation.ProfileViewModel
import ru.kpfu.itis.feature.response.presentation.ResponseViewModel

val viewModelModule = module {
    viewModel {
        AuthViewModel(loginUseCase = get(), registerUseCase = get())
    }
    viewModel {
        FeedViewModel(getCasesUseCase = get())
    }
    viewModel { (caseId: Long) ->
        CaseDetailViewModel(getCaseDetailUseCase = get(), caseId = caseId)
    }
    viewModel {
        ProfileViewModel(getProfileUseCase = get(), logoutUseCase = get())
    }
    viewModel {
        MyCasesViewModel(getMyCasesUseCase = get())
    }
    viewModel {
        ResponseViewModel(submitResponseUseCase = get())
    }
}
