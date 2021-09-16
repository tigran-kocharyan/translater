package ru.totowka.translator.data.repository

import io.reactivex.Single
import ru.totowka.translator.data.api.SkyEngApi
import ru.totowka.translator.domain.model.WordEntity
import ru.totowka.translator.domain.repository.TranslatorRepository
import javax.inject.Inject

class TranslatorRepositoryImpl @Inject constructor(private val skyEngApi: SkyEngApi) : TranslatorRepository {
    override fun getWordTranslation(word: String): Single<List<WordEntity>> {
        return skyEngApi.getWordTranslation(word).map { list -> list.map {it.toEntity()}}}
    }