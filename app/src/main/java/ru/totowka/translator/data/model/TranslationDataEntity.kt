package ru.totowka.translator.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import ru.totowka.translator.data.datastore.db.Converters
import ru.totowka.translator.data.datastore.db.DatabaseScheme
import ru.totowka.translator.domain.model.MeaningEntity
import ru.totowka.translator.domain.model.TranslationEntity
import ru.totowka.translator.domain.model.WordEntity

/**
 * Data-объект слова
 */
@Entity(tableName = DatabaseScheme.TranslationTableScheme.WORD_TABLE_NAME)
@TypeConverters(Converters::class)
@Parcelize
data class WordDataEntity(
    @PrimaryKey
    @SerializedName("id") var id: Int,
    @SerializedName("text") var text: String?,
//    @Embedded(prefix = "meaning")
    @SerializedName("meanings") var meanings: List<MeaningDataEntity>?
) : Parcelable {
    constructor() : this(0, "", null)

    fun toEntity() = WordEntity(
        id, text,
        meanings?.mapTo(arrayListOf(), { e -> e.toEntity() }),
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

/**
 * Data-объект значения слова
 */
@Entity(tableName = DatabaseScheme.TranslationTableScheme.MEANING_TABLE_NAME)
@TypeConverters(Converters::class)
@Parcelize
data class MeaningDataEntity(
    @PrimaryKey
    @SerializedName("id") var id: Int?,
    @SerializedName("partOfSpeechCode") var partOfSpeechCode: String?,
//    @Embedded(prefix = "translation")
    @SerializedName("translation") var translation: TranslationDataEntity?,
    @SerializedName("previewUrl") var previewUrl: String?,
    @SerializedName("imageUrl") var imageUrl: String?,
    @SerializedName("transcription") var transcription: String?,
    @SerializedName("soundUrl") var soundUrl: String?
) : Parcelable {
    constructor() : this(0, "", TranslationDataEntity(), "", "", "", "")

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

/**
 * Data-объект перевода слова
 */
@Entity(tableName = DatabaseScheme.TranslationTableScheme.TRANSLATION_TABLE_NAME)
@TypeConverters(Converters::class)
@Parcelize
data class TranslationDataEntity(
    @PrimaryKey
    @SerializedName("text") var text: String,
    @SerializedName("note") var note: String? = null
) : Parcelable {

    constructor() : this("", "")

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
