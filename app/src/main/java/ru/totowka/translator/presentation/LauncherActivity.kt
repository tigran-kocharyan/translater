package ru.totowka.translator.presentation

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import ru.totowka.translator.R
import ru.totowka.translator.presentation.wordlist.view.WordlistFragment

/**
 * Основное Activity, на которое помещаются фрагменты
 */
class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, WordlistFragment.newInstance(), WordlistFragment.TAG)
                .commit()
        }
    }
}