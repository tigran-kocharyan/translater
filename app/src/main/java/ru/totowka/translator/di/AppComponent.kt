package ru.totowka.translator.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.totowka.translator.presentation.LauncherActivity
import ru.totowka.translator.presentation.translate.view.TranslateFragment
import ru.totowka.translator.presentation.wordlist.view.WordlistFragment
import javax.inject.Singleton

/**
 * Общий компонент для всего приложения
 */
@Singleton
@Component(
    modules = [
        RetrofitModule::class, BindModule::class, RoomModule::class
    ]
)
interface AppComponent {
    fun inject(launcher: LauncherActivity)
    fun inject(fragment: WordlistFragment)
    fun inject(fragment: TranslateFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}