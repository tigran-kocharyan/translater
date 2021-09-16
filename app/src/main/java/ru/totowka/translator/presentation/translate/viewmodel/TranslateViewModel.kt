package ru.totowka.translator.presentation.translate.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.totowka.translator.domain.interactor.DictionaryInteractor
import ru.totowka.translator.domain.interactor.TranslationInteractor
import ru.totowka.translator.domain.model.WordEntity
import ru.totowka.translator.presentation.wordlist.viewmodel.WordlistViewModel
import ru.totowka.translator.utils.SchedulersProvider

class TranslateViewModel(
    private val dictionaryInteractor: DictionaryInteractor,
    private val translationInteractor: TranslationInteractor,
    private val schedulers: SchedulersProvider
) : ViewModel() {

    private val progressLiveData = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val translationsLiveData = MutableLiveData<List<WordEntity>>()
    private val disposables = CompositeDisposable()

    fun translate(input: String) = disposables.add(translationInteractor.getTranslation(input)
            .observeOn(schedulers.io()).subscribeOn(schedulers.io())
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribe(translationsLiveData::postValue, errorLiveData::postValue))

    fun addWord(wordEntity: WordEntity) = disposables.add(dictionaryInteractor.addWord(wordEntity)
        .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
        .doOnSubscribe { progressLiveData.postValue(true) }
        .doAfterTerminate { progressLiveData.postValue(false) }
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())
        .subscribe({ Log.d(DB, "completed addWord!") }, errorLiveData::postValue)
    )

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
        disposables.clear()
    }

    /**
     * @return LiveData<Boolean> для подписки
     */
    fun getProgressLiveData(): LiveData<Boolean> {
        return progressLiveData
    }

    /**
     * @return LiveData<Boolean> для подписки
     */
    fun getErrorLiveData(): LiveData<Throwable> {
        return errorLiveData
    }

    /**
     * @return LiveData<List<WordEntity>> для подписки
     */
    fun getWordsLiveData(): LiveData<List<WordEntity>> {
        return translationsLiveData
    }

    companion object {
        private const val RETROFIT = "RETROFIT"
        private const val DB = "DATABASE_CUSTOM"
    }
}