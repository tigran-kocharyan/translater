package ru.totowka.translator.domain.model

import android.os.Parcelable
import androidx.room.Ignore
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

/**
 * Domain-объект слова
 */
@Parcelize
data class WordEntity(
    var id: Int,
    var text: String? = null,
    var meanings: ArrayList<MeaningEntity>? = null,
    @IgnoredOnParcel @Transient @Ignore var isSelected: Boolean = false
) : Parcelable

/**
 * Domain-объект перевода слова
 */
@Parcelize
data class TranslationEntity(
    var text: String,
    var note: String? = null,
) : Parcelable

/**
 * Domain-объект значения слова
 */
@Parcelize
data class MeaningEntity(
    var id: Int? = null,
    var partOfSpeechCode: String? = null,
    var translation: TranslationEntity? = null,
    var previewUrl: String? = null,
    var imageUrl: String? = null,
    var transcription: String? = null,
    var soundUrl: String? = null
) : Parcelable