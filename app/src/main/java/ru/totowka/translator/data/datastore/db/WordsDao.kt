//package ru.totowka.translator.data.datastore.db
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import ru.totowka.translator.data.datastores.db.TranslationEntity
//
//@Dao
//interface WordsDao {
//        @Insert(onConflict = OnConflictStrategy.IGNORE)
//        fun initBooks(books: List<TranslationEntity>)
//
//        @Query("SELECT * FROM ${DatabaseScheme.TranslationTableScheme.TABLE_NAME}")
//        fun getAllBooks(): List<TranslationEntity>
//}