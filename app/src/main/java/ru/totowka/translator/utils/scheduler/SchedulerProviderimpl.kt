package ru.totowka.translator.utils.scheduler

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.totowka.translator.utils.scheduler.SchedulersProvider
import javax.inject.Inject

/**
 * Реализация интерфейса [SchedulersProvider], используемая в работе
 */
class SchedulersProviderImpl @Inject constructor(): SchedulersProvider {
    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}