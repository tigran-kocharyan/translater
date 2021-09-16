package ru.totowka.translator.domain.repository

import androidx.lifecycle.LiveData
import io.reactivex.Completable
import io.reactivex.Single
import ru.totowka.translator.domain.model.WordEntity

interface DictionaryRepository {
    fun update(word: WordEntity): Completable
    fun add(word: WordEntity): Completable

    fun delete(id: Int): Completable
    fun deleteAll(): Completable

    //    fun getWords(): Single<List<WordEntity>>
    fun get(id: Int): Single<WordEntity>
    fun getAll(): LiveData<List<WordEntity>>
}