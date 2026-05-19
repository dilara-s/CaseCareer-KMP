package ru.kpfu.itis.feature.feed.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kpfu.itis.feature.feed.data.remote.FeedApi
import ru.kpfu.itis.feature.feed.data.repository.FeedRepositoryImpl
import ru.kpfu.itis.feature.feed.domain.repository.FeedRepository
import ru.kpfu.itis.feature.feed.domain.usecase.GetCasesUseCase
import ru.kpfu.itis.feature.feed.presentation.FeedViewModel

val feedModule = module {
    single { FeedApi(get()) }
    singleOf(::FeedRepositoryImpl) bind FeedRepository::class
    factoryOf(::GetCasesUseCase)
    factory {
        FeedViewModel(
            getCasesUseCase = get(),
        )
    }
}