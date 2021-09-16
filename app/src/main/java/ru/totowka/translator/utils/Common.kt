package ru.totowka.translator.utils

import android.view.View

object Common {
    fun getPartOfSpeech(part: String) : String {
        return when(part) {
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

    fun View.setGone() {
        this.visibility = View.GONE
    }
}