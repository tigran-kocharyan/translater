package ru.totowka.translator.presentation.wordlist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.totowka.translator.domain.interactor.DictionaryInteractor
import ru.totowka.translator.domain.model.MeaningEntity
import ru.totowka.translator.domain.model.TranslationEntity
import ru.totowka.translator.domain.model.WordEntity
import ru.totowka.translator.utils.SchedulersProvider


class WordlistViewModel(
    private val dictionaryInteractor: DictionaryInteractor,
    private val schedulers: SchedulersProvider
) : ViewModel() {

    private val progressLiveData = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val disposables = CompositeDisposable()

    fun getWord(id: Int) = disposables.add(dictionaryInteractor.getWord(id)
        .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
        .doOnSubscribe { progressLiveData.postValue(true) }
        .doAfterTerminate { progressLiveData.postValue(false) }
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())
        .subscribe({ Log.d(DB, "completed getWord!") }, { t -> Log.d(DB, "error: $t") })
    )

    fun addWord(wordEntity: WordEntity) = disposables.add(dictionaryInteractor.addWord(wordEntity)
        .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
        .doOnSubscribe { progressLiveData.postValue(true) }
        .doAfterTerminate { progressLiveData.postValue(false) }
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())
        .subscribe({ Log.d(DB, "completed addWord!") }, { t -> Log.d(DB, "error: $t") })
    )

    fun updateWord(wordEntity: WordEntity) = disposables.add(dictionaryInteractor.updateWord(wordEntity)
        .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
        .doOnSubscribe { progressLiveData.postValue(true) }
        .doAfterTerminate { progressLiveData.postValue(false) }
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())
        .subscribe({ Log.d(DB, "completed updateWord!") }, { t -> Log.d(DB, "error: $t") })
    )

    fun deleteWord(id: Int) = disposables.add(dictionaryInteractor.deleteWord(id)
        .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
        .doOnSubscribe { progressLiveData.postValue(true) }
        .doAfterTerminate { progressLiveData.postValue(false) }
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())
        .subscribe({ Log.d(DB, "completed deleteWord!") }, { t -> Log.d(DB, "error: $t") })
    )

    fun deleteAllWords() = disposables.add(dictionaryInteractor.deleteAllWords()
        .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
        .doOnSubscribe { progressLiveData.postValue(true) }
        .doAfterTerminate { progressLiveData.postValue(false) }
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())
        .subscribe({ Log.d(DB, "completed deleteAllWords!") }, { t -> Log.d(DB, "error: $t") })
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
    fun getWordsLiveData() : LiveData<List<WordEntity>> {
        return dictionaryInteractor.getWords()
    }

    companion object {
        private const val RETROFIT = "RETROFIT"
        private const val DB = "DATABASE_CUSTOM"
    }
}