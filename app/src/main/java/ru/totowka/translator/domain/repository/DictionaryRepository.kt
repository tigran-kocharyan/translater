package ru.totowka.translator.domain.repository

import io.reactivex.Completable
import io.reactivex.Single
import ru.totowka.translator.domain.model.WordEntity

interface DictionaryRepository {
    fun add(word: WordEntity): Completable
    fun delete(id: Long): Completable
    fun deleteAll(): Completable
    fun getWords(): Single<List<WordEntity>>
}