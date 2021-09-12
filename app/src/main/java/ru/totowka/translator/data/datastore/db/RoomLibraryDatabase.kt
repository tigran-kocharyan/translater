//package ru.totowka.translator.data.datastore.db
//
//import androidx.room.Database
//import androidx.room.RoomDatabase
//import ru.totowka.translator.data.datastores.db.TranslationEntity
//
//@Database(
//    entities = [TranslationEntity::class],
//    version = DatabaseScheme.DB_VERSION,
//    exportSchema = true
//)
//abstract class RoomLibraryDatabase : RoomDatabase() {
//    abstract fun wordsDao(): WordsDao
//}