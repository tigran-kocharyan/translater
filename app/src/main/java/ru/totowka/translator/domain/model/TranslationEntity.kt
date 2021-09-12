package ru.totowka.translator.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data  class WordEntity(
    var id: Integer,
    var text: String? = null,
    var meanings: ArrayList<MeaningEntity>? = null
) : Parcelable

@Parcelize
data class TranslationEntity(
    var text: String? = null,
    var note: String? = null,
) : Parcelable

@Parcelize
data  class MeaningEntity(
    var id: Integer? = null,
    var partOfSpeechCode: String? = null,
    var translation: TranslationEntity? = null,
    var previewUrl: String? = null,
    var imageUrl: String? = null,
    var transcription: String? = null,
    var soundUrl: String? = null
) : Parcelable