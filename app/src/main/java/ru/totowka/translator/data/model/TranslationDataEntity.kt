package ru.totowka.translator.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import ru.totowka.translator.domain.model.MeaningEntity
import ru.totowka.translator.domain.model.TranslationEntity
import ru.totowka.translator.domain.model.WordEntity

@Entity
@Parcelize
data class WordDataEntity(
    @PrimaryKey
    @SerializedName("id") var id: Integer,
    @SerializedName("text") var text: String?,
    @SerializedName("meanings") var meanings: ArrayList<MeaningDataEntity>?
) : Parcelable {

    fun toEntity() = WordEntity(
        id, text,
        meanings?.mapTo(arrayListOf(), { e ->
            MeaningEntity(
                e.id,
                e.partOfSpeechCode,
                e.translation?.toEntity(),
                e.previewUrl,
                e.imageUrl,
                e.transcription,
                e.soundUrl
            )
        }),
    )

    companion object {
        fun fromEntity(entity: WordEntity) = with(entity) {
            WordDataEntity(
                id,
                text,
                meanings?.mapTo(arrayListOf(), { e ->
                    MeaningDataEntity.fromEntity(e)
                })
            )
        }
    }
}

@Entity
@Parcelize
data class MeaningDataEntity(
    @SerializedName("id") var id: Integer?,
    @SerializedName("partOfSpeechCode") var partOfSpeechCode: String?,
    @SerializedName("translation") var translation: TranslationDataEntity?,
    @SerializedName("previewUrl") var previewUrl: String?,
    @SerializedName("imageUrl") var imageUrl: String?,
    @SerializedName("transcription") var transcription: String?,
    @SerializedName("soundUrl") var soundUrl: String?
) : Parcelable {
    fun toEntity() = MeaningEntity(
        id, partOfSpeechCode,
        translation?.toEntity(), previewUrl, imageUrl, transcription, soundUrl
    )

    companion object {
        fun fromEntity(entity: MeaningEntity) = with(entity) {
            MeaningDataEntity(
                id, partOfSpeechCode,
                translation?.let { TranslationDataEntity.fromEntity(it) }, previewUrl, imageUrl, transcription, soundUrl
            )
        }
    }
}


@Entity
@Parcelize
data class TranslationDataEntity(
    @SerializedName("text") var text: String? = null,
    @SerializedName("note") var note: String? = null
) : Parcelable {
    fun toEntity() = TranslationEntity(text, note)

    companion object {
        fun fromEntity(entity: TranslationEntity) = with(entity) {
            TranslationDataEntity(
                text = text,
                note = note
            )
        }
    }
}
