package ru.totowka.translator.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import io.reactivex.Completable
import io.reactivex.Single
import ru.totowka.translator.data.datastore.db.WordsDao
import ru.totowka.translator.data.model.WordDataEntity
import ru.totowka.translator.domain.model.WordEntity
import ru.totowka.translator.domain.repository.DictionaryRepository
import javax.inject.Inject

class DictionaryRepositoryImpl @Inject constructor(private val wordsDao: WordsDao) : DictionaryRepository {
    override fun update(word: WordEntity) =
        Completable.fromRunnable { wordsDao.updateWord(WordDataEntity.fromEntity(word)) }

    override fun add(word: WordEntity) =
        Completable.fromRunnable { wordsDao.addWord(WordDataEntity.fromEntity(word)) }

    override fun delete(id: Int) = Completable.fromCallable { wordsDao.deleteSingleWord(id) }

    override fun deleteAll() = Completable.fromCallable { wordsDao.deleteAllWords() }

//    override fun getWords(): Single<List<WordEntity>> {
//        return Single.fromCallable{wordsDao.getAllWords().map { it.toEntity() }}
//    }

    override fun getAll(): LiveData<List<WordEntity>> {
        return Transformations.map(wordsDao.getAllWords()) { it.map { word -> word.toEntity() } }
    }

    override fun get(id: Int): Single<WordEntity> {
        return Single.fromCallable { wordsDao.getWord(id).toEntity() }
    }
}