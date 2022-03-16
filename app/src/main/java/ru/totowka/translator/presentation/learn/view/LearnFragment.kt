package ru.totowka.translator.presentation.learn.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.totowka.translator.R
import ru.totowka.translator.databinding.FragmentLearnBinding
import ru.totowka.translator.domain.model.WordEntity
import ru.totowka.translator.utils.Common.string

/**
 * Фрагмент, отвечающий за экран изучения слов.
 */
class LearnFragment : Fragment() {
    private lateinit var binding: FragmentLearnBinding
    var words: ArrayList<WordEntity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        words = arguments?.getParcelableArrayList(WORDS_TAG)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLearnBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        supportBackIcon(view)
    }

    private fun supportBackIcon(view: View) {
        this.setHasOptionsMenu(true)
        (activity as AppCompatActivity).apply {
            this.setSupportActionBar(view.findViewById(R.id.learn_toolbar))
            this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            this.supportActionBar?.setDisplayShowHomeEnabled(true)
            this.supportActionBar?.title = string(R.string.learn_name)
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
        const val TAG = "LearnFragment"
        const val WORDS_TAG = "WORDS_TAG"
        private const val TAG_ADD = "$TAG ADD"
        private const val TAG_ERROR = "$TAG ERROR"
        private const val TAG_PROGRESS = "$TAG PROGRESS"

        /**
         * Получение объекта [LearnFragment]
         */
        fun newInstance(words: List<WordEntity>) = LearnFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(WORDS_TAG, ArrayList(words))
            }
        }
    }
}