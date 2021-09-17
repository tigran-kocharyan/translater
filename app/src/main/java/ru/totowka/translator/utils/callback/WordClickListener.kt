package ru.totowka.translator.utils.callback

import ru.totowka.translator.domain.model.WordEntity

/**
 * Интерфейс для работы с нажатием на элемент RecyclerView
 */
interface WordClickListener {
    fun onClick(word: WordEntity)
}