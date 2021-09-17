package ru.totowka.translator.utils.scheduler

import io.reactivex.Scheduler

/**
 * Интерфейс для работы с Schedulers
 */
interface SchedulersProvider {
    /**
     * IO-поток
     */
    fun io(): Scheduler

    /**
     * UI-поток
     */
    fun ui(): Scheduler
}