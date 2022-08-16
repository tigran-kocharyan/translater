package ru.totowka.translator.presentation.learn.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import ru.totowka.translator.R
import ru.totowka.translator.databinding.FragmentLearnlistBinding
import ru.totowka.translator.domain.model.LearnEntity
import ru.totowka.translator.domain.model.LearnEntity.Companion.fromEntity
import ru.totowka.translator.domain.model.WordEntity
import ru.totowka.translator.presentation.SharedViewModel
import ru.totowka.translator.presentation.learn.adapter.LearnAdapter
import ru.totowka.translator.utils.Common.string
import ru.totowka.translator.utils.callback.LearnWordClickListener

class LearnlistFragment : Fragment() {
    private lateinit var binding: FragmentLearnlistBinding
    private lateinit var adapter: LearnAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var clickListener = object : LearnWordClickListener {
        override fun onClick(learn: LearnEntity) {
            sharedViewModel.setLearn(learn)
            findNavController().navigate(R.id.action_learnlistFragment_to_explanationFragment)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLearnlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        supportBackIcon(view)
        createAdapter()
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

    private fun createAdapter() {
        adapter = LearnAdapter(sharedViewModel?.learns?.value ?: emptyList(), clickListener)
        binding.wordlist.layoutManager = LinearLayoutManager(context)
        binding.wordlist.adapter = adapter
        binding.wordlist.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    private fun exit() {
        (activity as AppCompatActivity).supportFragmentManager
            .popBackStack()
    }

    companion object {
        const val TAG = "LearnlistFragment"
        private const val TAG_ADD = "$TAG ADD"
        private const val TAG_ERROR = "$TAG ERROR"
        private const val TAG_PROGRESS = "$TAG PROGRESS"

        /**
         * Получение объекта [LearnlistFragment]
         */
        fun newInstance() = LearnlistFragment()
    }
}