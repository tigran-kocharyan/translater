package ru.totowka.translator.presentation.wordlist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.totowka.translator.domain.interactor.DictionaryInteractor
import ru.totowka.translator.domain.model.WordEntity
import ru.totowka.translator.utils.scheduler.SchedulersProvider

/**
 * ViewModel для фрагмента [WordlistFragment]
 */
class WordlistViewModel(
    private val dictionaryInteractor: DictionaryInteractor,
    private val schedulers: SchedulersProvider
) : ViewModel() {

    private val progressLiveData = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val wordLiveData = MutableLiveData<WordEntity>()
    private val disposables = CompositeDisposable()

    /**
     * Получить слово с id=[id] из БД
     *
     * @param id идентификатор
     */
    fun getWord(id: Int) {
        disposables.add(dictionaryInteractor.getWord(id)
            .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe(wordLiveData::setValue, errorLiveData::setValue)
        )
    }

    /**
     * Обновить слово [wordEntity] в БД
     *
     * @param wordEntity слово
     */
    fun addWord(wordEntity: WordEntity) {
        disposables.add(dictionaryInteractor.addWord(wordEntity)
            .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe({ Log.d(DB, "completed addWord!") }, errorLiveData::setValue)
        )
    }

    /**
     * Обновить слово с wordEntity=[wordEntity] в БД
     *
     * @param wordEntity слово
     */
    fun updateWord(wordEntity: WordEntity) {
        disposables.add(dictionaryInteractor.updateWord(wordEntity)
            .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe({ Log.d(DB, "completed updateWord!") }, errorLiveData::setValue)
        )
    }

    /**
     * Удалить слово с id=[id] из БД
     *
     * @param id идентификатор
     */
    fun deleteWord(id: Int) {
        disposables.add(dictionaryInteractor.deleteWord(id)
            .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe({ Log.d(DB, "completed deleteWord!") }, errorLiveData::setValue)
        )
    }

    /**
     * Удалить все слова из БД
     */
    fun deleteAllWords() {
        disposables.add(dictionaryInteractor.deleteAllWords()
            .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe({ Log.d(DB, "completed deleteAllWords!") }, errorLiveData::setValue)
        )
    }

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
        return dictionaryInteractor.getWords()
    }

    /**
     * @return LiveData<WordEntity> для подписки на получение конкретного слова из БД
     */
    fun getWordLiveData(): LiveData<WordEntity> {
        return wordLiveData
    }

    companion object {
        private const val RETROFIT = "RETROFIT"
        private const val DB = "DATABASE_CUSTOM"
    }
}