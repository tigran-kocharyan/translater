package ru.totowka.translator.utils

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import androidx.annotation.StringRes

/**
 * Объект для работы с методами во время UI
 */
object Common {

    /**
     * Перевод SkyEng аббревиатуры в полную форму
     *
     * @param part часть речи
     */
    fun getPartOfSpeech(part: String): String {
        return when (part) {
            "n" -> "noun"
            "v" -> "verb"
            "j" -> "adjective"
            "r" -> "adverb"
            "prp" -> "preposition"
            "prn" -> "pronoun"
            "crd" -> "cardinal number"
            "cjc" -> "conjunction"
            "exc" -> "interjection"
            "det" -> "article"
            "abb" -> "abbreviation"
            "x" -> "particle"
            "ord" -> "ordinal number"
            "md" -> "modal verb"
            "ph" -> "phrase"
            "phi" -> "idiom"
            else -> "undefined"
        }
    }

    /**
     * Проверка на подключени к интернету
     *
     * @param context контекст приложения
     */
    fun isConnectedInternet(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    /**
     * Перевод View в состояние GONE
     */
    fun View.setGone() {
        this.visibility = View.GONE
    }

    /**
     * Получение строки из ресурсов
     */
    fun Context.string(@StringRes resId: Int) = this.resources.getString(resId)
}