package ru.totowka.translator.data.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.totowka.translator.data.model.WordDataEntity

/**
 * Интерфейс для общения с API SkyEng
 */
interface SkyEngApi {
    @GET("api/public/v1/words/search")
    fun getWordTranslation(@Query("search") word: String): Single<List<WordDataEntity>>
}