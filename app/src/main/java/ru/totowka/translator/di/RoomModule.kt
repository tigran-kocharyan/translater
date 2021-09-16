package ru.totowka.translator.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.totowka.translator.data.datastore.db.DatabaseScheme
import ru.totowka.translator.data.datastore.db.RoomDictionaryDatabase
import ru.totowka.translator.data.datastore.db.WordsDao
import javax.inject.Singleton

@Module
class RoomModule {
    @Provides
    @Singleton
    fun provideRoomDatabase(context: Context): RoomDictionaryDatabase = Room.databaseBuilder(
        context,
        RoomDictionaryDatabase::class.java,
        DatabaseScheme.DB_NAME
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideWordsDao(db: RoomDictionaryDatabase): WordsDao = db.wordsDao()
}