package ru.totowka.translator.domain.repository

import io.reactivex.Single
import ru.totowka.translator.domain.model.WordEntity

/**
 * Интерфейс для взаимодействия с репозиторием работы перевода
 */
interface TranslatorRepository {
    /**
     * Получение перевода слова
     */
    fun getWordTranslation(word: String): Single<List<WordEntity>>
}