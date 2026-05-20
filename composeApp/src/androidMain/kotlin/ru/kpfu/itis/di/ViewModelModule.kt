package ru.kpfu.itis.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.kpfu.itis.feature.auth.presentation.AuthViewModel
import ru.kpfu.itis.feature.feed.presentation.caseDetail.CaseDetailViewModel
import ru.kpfu.itis.feature.feed.presentation.feed.FeedViewModel

val viewModelModule = module {
    viewModel {
        AuthViewModel(
            loginUseCase = get(),
            registerUseCase = get()
        )
    }
    viewModel {
        FeedViewModel(getCasesUseCase = get())
    }
    viewModel { (caseId: Long) ->
        CaseDetailViewModel(getCaseDetailUseCase = get(), caseId = caseId)
    }
}