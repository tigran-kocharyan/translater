package ru.totowka.translator.data.datastore.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.totowka.translator.data.model.MeaningDataEntity
import ru.totowka.translator.data.model.TranslationDataEntity
import ru.totowka.translator.data.model.WordDataEntity

@Database(
    entities = [WordDataEntity::class, TranslationDataEntity::class, MeaningDataEntity::class],
    version = DatabaseScheme.DB_VERSION,
    exportSchema = true
)
abstract class RoomDictionaryDatabase : RoomDatabase() {
    abstract fun wordsDao(): WordsDao
}