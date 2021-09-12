package ru.totowka.translator.di

import dagger.Component
import ru.totowka.translator.presentation.LauncherActivity
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RetrofitModule::class, BindModule::class
    ]
)
interface AppComponent {
    fun inject(launcher: LauncherActivity)
}