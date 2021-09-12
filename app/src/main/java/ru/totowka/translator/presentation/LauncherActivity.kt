package ru.totowka.translator.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.totowka.translator.App
import ru.totowka.translator.R
import ru.totowka.translator.di.DaggerAppComponent
import ru.totowka.translator.domain.interactor.TranslationInteractor
import javax.inject.Inject

class LauncherActivity : AppCompatActivity() {

    @Inject
    lateinit var interactor : TranslationInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (this.applicationContext as App).appComp().inject(this)

        interactor.getTranslation("гей")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ Log.d(TAG, "onSuccess: $it")}, { Log.d(TAG, "onError: ${it.message}")})
    }


    companion object {
        private const val TAG = "Retrofit"
    }
}