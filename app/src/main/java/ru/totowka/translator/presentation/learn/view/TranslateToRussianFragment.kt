package ru.totowka.translator.presentation.learn.view

import android.os.Bundle
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
import ru.totowka.translator.databinding.FragmentLearnBinding
import ru.totowka.translator.databinding.FragmentTranslateToEnglishBinding
import ru.totowka.translator.databinding.FragmentTranslateToRussianBinding
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
class TranslateToRussianFragment : Fragment() {
    private lateinit var binding: FragmentTranslateToRussianBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var learn: LearnEntity? = null
    private var learns: ArrayList<LearnEntity>? = null
    private var learnPosition: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTranslateToRussianBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        supportBackIcon(view)
        learn = sharedViewModel.learn?.value
        sharedViewModel.learns?.let { learns = ArrayList(it.value) }
        learnPosition = sharedViewModel.learnPosition?.value
        binding.fabNext.setOnClickListener {
            if (binding.wordInput.editText?.text.toString().trim().lowercase() ==
                learn?.word?.meanings?.first()?.translation?.text?.trim()?.lowercase()
            ) {
                learns?.let {
                    val position = it.indexOfFirst { search ->
                        search == learn
                    }
                    it[position].state.isLearned = true
                    sharedViewModel.setLearns(it)
                }
                findNavController().navigate(R.id.action_translateToRussianFragment_to_learnlistFragment)
            } else {
                Snackbar.make(binding.root, "Wrong answer!", BaseTransientBottomBar.LENGTH_SHORT)
                    .show()
            }
        }
        binding.toolbar.title =
            getString(R.string.translate_to_russian, learn?.word?.text ?: R.string.undefined)
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
        const val TAG = "TranslateToRussianFragment"
        private const val TAG_ADD = "$TAG ADD"
        private const val TAG_ERROR = "$TAG ERROR"
        private const val TAG_PROGRESS = "$TAG PROGRESS"

        /**
         * Получение объекта [TranslateToRussianFragment]
         */
        fun newInstance() = TranslateToRussianFragment()
    }
}