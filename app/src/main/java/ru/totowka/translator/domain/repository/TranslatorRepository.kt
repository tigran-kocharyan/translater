package ru.totowka.translator.domain.repository

import io.reactivex.Single
import ru.totowka.translator.domain.model.WordEntity


interface TranslatorRepository {
    fun getWordTranslation(word: String): Single<List<WordEntity>>
}