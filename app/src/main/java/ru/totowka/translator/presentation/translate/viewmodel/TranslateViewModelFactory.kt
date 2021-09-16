package ru.totowka.translator.presentation.translate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.totowka.translator.domain.interactor.DictionaryInteractor
import ru.totowka.translator.domain.interactor.TranslationInteractor
import ru.totowka.translator.utils.SchedulersProvider

class TranslateViewModelFactory (private val dictionaryInteractor: DictionaryInteractor,
                                 private val translationInteractor: TranslationInteractor,
                                 private val schedulersProvider: SchedulersProvider
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(aClass: Class<T>): T {
        return TranslateViewModel(
            dictionaryInteractor, translationInteractor, schedulersProvider
        ) as T
    }
}
