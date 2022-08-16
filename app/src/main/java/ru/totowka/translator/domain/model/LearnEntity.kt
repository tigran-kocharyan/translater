package ru.totowka.translator.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.totowka.translator.data.model.MeaningDataEntity
import ru.totowka.translator.data.model.WordDataEntity

/**
 * Domain-объект слова
 */
@Parcelize
data class LearnEntity(var word: WordEntity, var state: LearnState = LearnState()) : Parcelable {
    companion object {
        fun WordEntity.fromEntity() = LearnEntity(this, LearnState(false))
    }
}

/**
 * Domain-объект состояния изучения слова
 */
@Parcelize
data class LearnState(var isLearned: Boolean = false) : Parcelable
