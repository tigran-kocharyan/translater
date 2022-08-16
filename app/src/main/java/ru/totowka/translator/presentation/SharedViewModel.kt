package ru.totowka.translator.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.totowka.translator.domain.model.LearnEntity
import ru.totowka.translator.domain.model.WordEntity

class SharedViewModel : ViewModel() {
    private var _words = MutableLiveData(emptyList<WordEntity>())
    private var _learns = MutableLiveData(emptyList<LearnEntity>())
    private var _learn = MutableLiveData(LearnEntity(word = WordEntity(0)))
    private var _learnPosition = MutableLiveData(0)

    var words: LiveData<List<WordEntity>>? = _words
    var learns: LiveData<List<LearnEntity>>? = _learns
    var learn: LiveData<LearnEntity>? = _learn
    var learnPosition: LiveData<Int>? = _learnPosition

    fun setWords(words: List<WordEntity>) {
        _words.value = words
        _learns.value = words.map { LearnEntity(it) }
    }

    fun setLearns(learns: List<LearnEntity>) {
        _learns.value = learns
    }

    fun setLearn(learn: LearnEntity) {
        _learn.value = learn
    }
    fun setLearnPosition(position: Int) {
        _learnPosition.value = position
    }

}