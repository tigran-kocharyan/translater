package ru.totowka.translator.data.datastore.db

import androidx.room.TypeConverter
import com.google.gson.Gson

import ru.totowka.translator.data.model.MeaningDataEntity
import ru.totowka.translator.data.model.TranslationDataEntity

/**
 * Конвертеры для базы данных
 */
object Converters {
    @TypeConverter
    fun meaningsToJson(value: List<MeaningDataEntity>) = Gson().toJson(value)

    @TypeConverter
    fun jsonToMeanings(value: String) = Gson().fromJson(value, Array<MeaningDataEntity>::class.java).toList()

    @TypeConverter
    fun translationToJson(value: TranslationDataEntity?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToTranslation(value: String) = Gson().fromJson(value, TranslationDataEntity::class.java)
}