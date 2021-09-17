package ru.totowka.translator.data.datastore.db

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.totowka.translator.data.model.WordDataEntity

/**
 * Интерфейс для общения с базой данных. Названия функция и QUERY запрос описывают их лучше всего.
 */
@Dao
interface WordsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addWord(book: WordDataEntity)

//        @Query("SELECT * FROM ${DatabaseScheme.TranslationTableScheme.WORD_TABLE_NAME}")
//        fun getAllWords(): List<WordDataEntity>

    @Query("SELECT * FROM ${DatabaseScheme.TranslationTableScheme.WORD_TABLE_NAME}")
    fun getAllWords(): LiveData<List<WordDataEntity>>

    @Transaction
    @Query("SELECT * FROM ${DatabaseScheme.TranslationTableScheme.WORD_TABLE_NAME} WHERE id = :id ORDER BY id DESC")
    fun getWord(id: Int): WordDataEntity

    @Update
    fun updateWord(word: WordDataEntity)

    @Query("DELETE FROM ${DatabaseScheme.TranslationTableScheme.WORD_TABLE_NAME} WHERE id = :id")
    fun deleteSingleWord(id: Int)

    @Query("DELETE FROM ${DatabaseScheme.TranslationTableScheme.WORD_TABLE_NAME}")
    fun deleteAllWords()
}