package ru.totowka.translator.utils.scheduler

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import ru.totowka.translator.utils.scheduler.SchedulersProvider

/**
 * Реализация интерфейса [SchedulersProvider], используемая при тестировании
 */
class SchedulersProviderImplStub : SchedulersProvider {
    override fun io(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun ui(): Scheduler {
        return Schedulers.trampoline()
    }
}