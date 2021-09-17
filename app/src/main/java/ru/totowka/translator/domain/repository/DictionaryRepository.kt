package ru.totowka.translator.domain.repository

import androidx.lifecycle.LiveData
import io.reactivex.Completable
import io.reactivex.Single
import ru.totowka.translator.domain.model.WordEntity

/**
 * Интерфейс для взаимодействия с репозиторием работы словаря
 */
interface DictionaryRepository {
    /**
     * Обновление слова в БД
     */
    fun update(word: WordEntity): Completable
    /**
     * Добавление слова в БД
     */
    fun add(word: WordEntity): Completable

    /**
     * Удаление конкретного слова из БД
     */
    fun delete(id: Int): Completable
    /**
     * Удаление всех слов из БД
     */
    fun deleteAll(): Completable

    //    fun getWords(): Single<List<WordEntity>>

    /**
     * Получение конкретного слова из БД
     */
    fun get(id: Int): Single<WordEntity>
    /**
     * Получение всех слов из БД
     */
    fun getAll(): LiveData<List<WordEntity>>
}