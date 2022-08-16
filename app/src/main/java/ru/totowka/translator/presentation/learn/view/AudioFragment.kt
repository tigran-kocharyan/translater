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
import ru.totowka.translator.R
import ru.totowka.translator.databinding.FragmentAudioBinding
import ru.totowka.translator.databinding.FragmentLearnBinding
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
class AudioFragment : Fragment() {
    private lateinit var binding: FragmentAudioBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    var words: ArrayList<WordEntity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAudioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        supportBackIcon(view)
        binding.fabNext.setOnClickListener {
            findNavController().navigate(R.id.action_audioFragment_to_learnlistFragment)
        }
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
        const val TAG = "AudioFragment"
        private const val TAG_ADD = "$TAG ADD"
        private const val TAG_ERROR = "$TAG ERROR"
        private const val TAG_PROGRESS = "$TAG PROGRESS"

        /**
         * Получение объекта [AudioFragment]
         */
        fun newInstance() = AudioFragment()
    }
}