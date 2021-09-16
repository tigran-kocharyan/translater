package ru.totowka.translator.data.datastore.db

object DatabaseScheme {
    const val DB_NAME = "dictionary"
    const val DB_VERSION = 2

    object TranslationTableScheme {
        const val WORD_TABLE_NAME = "words"
        const val TRANSLATION_TABLE_NAME = "translations"
        const val MEANING_TABLE_NAME = "meanings"
    }
}

