package ru.totowka.translator.domain.interactor

import ru.totowka.translator.domain.model.WordEntity
import ru.totowka.translator.domain.repository.DictionaryRepository
import javax.inject.Inject

class DictionaryInteractor @Inject constructor(private val dictionaryRepository: DictionaryRepository) {
    fun getWords() = dictionaryRepository.getAll()
    fun getWord(id: Int) = dictionaryRepository.get(id)

    fun deleteAllWords() = dictionaryRepository.deleteAll()
    fun deleteWord(id: Int) = dictionaryRepository.delete(id)

    fun addWord(wordEntity: WordEntity) = dictionaryRepository.add(wordEntity)
    fun updateWord(wordEntity: WordEntity) = dictionaryRepository.update(wordEntity)
}