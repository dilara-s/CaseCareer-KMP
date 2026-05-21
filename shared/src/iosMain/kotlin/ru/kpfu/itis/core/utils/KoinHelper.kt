package ru.kpfu.itis.core.utils

import org.koin.core.parameter.parametersOf
import org.koin.mp.KoinPlatform
import ru.kpfu.itis.feature.auth.presentation.AuthViewModel
import ru.kpfu.itis.feature.feed.presentation.caseDetail.CaseDetailViewModel
import ru.kpfu.itis.feature.feed.presentation.feed.FeedViewModel
import ru.kpfu.itis.feature.mycases.presentation.MyCasesViewModel
import ru.kpfu.itis.feature.profile.presentation.ProfileViewModel
import ru.kpfu.itis.feature.response.presentation.ResponseViewModel

object KoinHelper {
    fun getAuthViewModel(): AuthViewModel = KoinPlatform.getKoin().get()
    fun getProfileViewModel(): ProfileViewModel = KoinPlatform.getKoin().get()
    fun getMyCasesViewModel(): MyCasesViewModel = KoinPlatform.getKoin().get()
    fun getResponseViewModel(): ResponseViewModel = KoinPlatform.getKoin().get()
    fun getFeedViewModel(): FeedViewModel = KoinPlatform.getKoin().get()
    fun getCaseDetailViewModel(caseId: Long): CaseDetailViewModel =
        KoinPlatform.getKoin().get(parameters = { parametersOf(caseId) })
}
