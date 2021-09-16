package ru.totowka.translator

import android.app.Application
import ru.totowka.translator.di.AppComponent
import ru.totowka.translator.di.DaggerAppComponent

class App() : Application() {
    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().context(applicationContext).build()
    }

    fun appComp() = appComponent
}
