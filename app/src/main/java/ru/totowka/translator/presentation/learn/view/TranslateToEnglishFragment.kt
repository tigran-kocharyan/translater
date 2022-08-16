package ru.totowka.translator.presentation.learn.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import ru.totowka.translator.R
import ru.totowka.translator.databinding.FragmentAudioBinding
import ru.totowka.translator.databinding.FragmentLearnBinding
import ru.totowka.translator.databinding.FragmentTranslateToEnglishBinding
import ru.totowka.translator.domain.model.LearnEntity
import ru.totowka.translator.domain.model.LearnEntity.Companion.fromEntity
import ru.totowka.translator.domain.model.WordEntity
import ru.totowka.translator.presentation.SharedViewModel
import ru.totowka.translator.presentation.learn.adapter.LearnAdapter
import ru.totowka.translator.presentation.wordinfo.WordDetailsBottomDialogFragment
import ru.totowka.translator.presentation.wordlist.adapter.WordlistAdapter
import ru.totowka.translator.utils.Common.string
import ru.totowka.translator.utils.callback.SwipeToDeleteCallback
import ru.totowka.translator.utils.callback.WordClickListener

/**
 * Фрагмент, отвечающий за экран изучения слов.
 */
class TranslateToEnglishFragment : Fragment() {
    private lateinit var binding: FragmentTranslateToEnglishBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    var words: ArrayList<WordEntity>? = null
    var learn: LearnEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTranslateToEnglishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        supportBackIcon(view)
        binding.fabNext.setOnClickListener {
            if (binding.wordInput.editText?.text.toString().trim().lowercase() ==
                learn?.word?.text?.trim()?.lowercase()
            ) {
                findNavController().navigate(R.id.action_translateToEnglishFragment_to_translateToRussianFragment)
            } else {
                Snackbar.make(binding.root, "Wrong answer!", BaseTransientBottomBar.LENGTH_SHORT)
                    .show()
            }
        }
        learn = sharedViewModel.learn?.value
        binding.toolbar.title = getString(
            R.string.translate_to_english,
            learn?.word?.meanings?.first()?.translation?.text ?: R.string.undefined
        )
    }

    private fun supportBackIcon(view: View) {
        this.setHasOptionsMenu(true)
        (activity as AppCompatActivity).apply {
            this.setSupportActionBar(view.findViewById(R.id.learn_toolbar))
            this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            this.supportActionBar?.setDisplayShowHomeEnabled(true)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                exit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun exit() {
        (activity as AppCompatActivity).supportFragmentManager
            .popBackStack()
    }

    companion object {
        const val TAG = "TranslateToEnglishFragment"
        private const val TAG_ADD = "$TAG ADD"
        private const val TAG_ERROR = "$TAG ERROR"
        private const val TAG_PROGRESS = "$TAG PROGRESS"

        /**
         * Получение объекта [TranslateToEnglishFragment]
         */
        fun newInstance() = TranslateToEnglishFragment()
    }
}