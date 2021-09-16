package ru.totowka.translator.utils.callback

import ru.totowka.translator.domain.model.WordEntity

interface WordClickListener {
    fun onClick(word: WordEntity)
}