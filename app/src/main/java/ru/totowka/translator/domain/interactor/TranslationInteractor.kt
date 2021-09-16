package ru.totowka.translator.domain.interactor

import io.reactivex.Single
import ru.totowka.translator.domain.model.WordEntity
import ru.totowka.translator.domain.repository.TranslatorRepository
import javax.inject.Inject

class TranslationInteractor @Inject constructor(private val translatorRepository: TranslatorRepository) {
    fun getTranslation(word: String) : Single<List<WordEntity>> {
        return translatorRepository.getWordTranslation(word)
    }
}