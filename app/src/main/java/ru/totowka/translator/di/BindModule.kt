package ru.totowka.translator.di

import androidx.room.RoomDatabase
import dagger.Binds
import dagger.Module
import ru.totowka.translator.data.datastore.db.RoomDictionaryDatabase
import ru.totowka.translator.data.repository.DictionaryRepositoryImpl
import ru.totowka.translator.data.repository.TranslatorRepositoryImpl
import ru.totowka.translator.domain.repository.DictionaryRepository
import ru.totowka.translator.domain.repository.TranslatorRepository
import ru.totowka.translator.utils.SchedulersProvider
import ru.totowka.translator.utils.SchedulersProviderImpl

@Module
interface BindModule {
    @Binds
    fun bindTranslationRepository(impl: TranslatorRepositoryImpl): TranslatorRepository

    @Binds
    fun bindDictionaryRepository(impl: DictionaryRepositoryImpl): DictionaryRepository

    @Binds
    fun bindRoomDatabase(impl: RoomDictionaryDatabase): RoomDatabase

    @Binds
    fun bindSchedulers(impl: SchedulersProviderImpl): SchedulersProvider
}