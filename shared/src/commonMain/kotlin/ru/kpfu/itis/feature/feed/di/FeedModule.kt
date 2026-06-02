package ru.kpfu.itis.feature.feed.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kpfu.itis.feature.feed.data.remote.FeedApi
import ru.kpfu.itis.feature.feed.data.repository.FeedRepositoryImpl
import ru.kpfu.itis.feature.feed.domain.repository.FeedRepository
import ru.kpfu.itis.feature.feed.domain.usecase.GetCaseDetailUseCase
import ru.kpfu.itis.feature.feed.domain.usecase.GetCasesUseCase
import ru.kpfu.itis.feature.feed.presentation.caseDetail.CaseDetailViewModel
import ru.kpfu.itis.feature.feed.presentation.feed.FeedViewModel

val feedModule =
    module {
        single { FeedApi(get()) }
        singleOf(::FeedRepositoryImpl) bind FeedRepository::class
        factoryOf(::GetCasesUseCase)
        factoryOf(::GetCaseDetailUseCase)
        factory {
            FeedViewModel(getCasesUseCase = get())
        }
        factory { (caseId: Long) ->
            CaseDetailViewModel(getCaseDetailUseCase = get(), caseId = caseId)
        }
    }
