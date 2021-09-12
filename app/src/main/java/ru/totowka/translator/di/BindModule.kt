package ru.totowka.translator.di

import dagger.Binds
import dagger.Module
import ru.totowka.translator.data.repository.TranslatorRepositoryImpl
import ru.totowka.translator.domain.repository.TranslatorRepository

@Module
interface BindModule {
    @Binds
    fun bindTranslationRepository(impl: TranslatorRepositoryImpl): TranslatorRepository
}