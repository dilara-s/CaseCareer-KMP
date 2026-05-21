package ru.kpfu.itis.core.mock

import ru.kpfu.itis.feature.mycases.domain.model.MyCase

internal object MockResponseStore {
    private val responses = mutableListOf<MyCase>()

    fun add(myCase: MyCase) {
        responses.removeAll { it.caseId == myCase.caseId }
        responses.add(0, myCase)
    }

    fun getAll(): List<MyCase> = responses.toList()
}
