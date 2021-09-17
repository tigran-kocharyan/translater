package ru.totowka.translator.domain.interactor

import ru.totowka.translator.domain.model.WordEntity
import ru.totowka.translator.domain.repository.DictionaryRepository
import javax.inject.Inject

/**
 * Класс для взаимодействиями с репозиториями словаря из ViewModel
 */
class DictionaryInteractor @Inject constructor(private val dictionaryRepository: DictionaryRepository) {
    /**
     * Получить все слова из БД
     */
    fun getWords() = dictionaryRepository.getAll()
    /**
     * Получить слова по [id] из БД
     *
     * @param id идентификатор элемента
     */
    fun getWord(id: Int) = dictionaryRepository.get(id)

    /**
     * Удалить все слова из БД
     */
    fun deleteAllWords() = dictionaryRepository.deleteAll()
    /**
     * Удалить слово по [id] из БД
     *
     * @param id идентификатор элемента
     */
    fun deleteWord(id: Int) = dictionaryRepository.delete(id)

    /**
     * Добавить слово [wordEntity] в БД
     *
     * @param wordEntity слово
     */
    fun addWord(wordEntity: WordEntity) = dictionaryRepository.add(wordEntity)
    /**
     * Обновить слово [wordEntity] в БД
     *
     * @param wordEntity слово
     */
    fun updateWord(wordEntity: WordEntity) = dictionaryRepository.update(wordEntity)
}