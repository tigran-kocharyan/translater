package ru.totowka.translator.presentation.wordlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.totowka.translator.domain.interactor.DictionaryInteractor
import ru.totowka.translator.utils.scheduler.SchedulersProvider

/**
 * Фабрика для ViewModel [WordlistViewModel]
 */
class WordlistViewModelFactory(
    private val dictionaryInteractor: DictionaryInteractor,
    private val schedulersProvider: SchedulersProvider
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(aClass: Class<T>): T {
        return WordlistViewModel(
            dictionaryInteractor,
            schedulersProvider
        ) as T
    }
}
