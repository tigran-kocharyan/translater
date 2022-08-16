package ru.totowka.translator.utils.callback

import ru.totowka.translator.domain.model.LearnEntity

/**
 * Интерфейс для работы с нажатием на элемент RecyclerView
 */
interface LearnWordClickListener {
    fun onClick(word: LearnEntity)
}