package ru.totowka.translator.utils

import io.reactivex.Scheduler

interface SchedulersProvider {
    fun io(): Scheduler
    fun ui(): Scheduler
}