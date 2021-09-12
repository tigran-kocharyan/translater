package ru.totowka.translator.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import ru.totowka.translator.domain.model.WordEntity
import ru.totowka.translator.domain.repository.DictionaryRepository

class DictionaryRepositoryImpl : DictionaryRepository {
    override fun add(word: WordEntity): Completable {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long): Completable {
        TODO("Not yet implemented")
    }

    override fun deleteAll(): Completable {
        TODO("Not yet implemented")
    }

    override fun getWords(): Single<List<WordEntity>> {
        TODO("Not yet implemented")
    }
}