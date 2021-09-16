package ru.totowka.translator.presentation.wordlist.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import ru.totowka.translator.App
import ru.totowka.translator.R
import ru.totowka.translator.databinding.FragmentWordlistBinding
import ru.totowka.translator.domain.interactor.DictionaryInteractor
import ru.totowka.translator.domain.model.WordEntity
import ru.totowka.translator.presentation.translate.view.TranslateFragment
import ru.totowka.translator.presentation.wordinfo.WordDetailsBottomDialogFragment
import ru.totowka.translator.presentation.wordlist.adapter.WordlistAdapter
import ru.totowka.translator.presentation.wordlist.viewmodel.WordlistViewModel
import ru.totowka.translator.presentation.wordlist.viewmodel.WordlistViewModelFactory
import ru.totowka.translator.utils.SchedulersProvider
import ru.totowka.translator.utils.callback.SwipeToDeleteCallback
import ru.totowka.translator.utils.callback.WordClickListener
import javax.inject.Inject

class WordlistFragment : Fragment() {
    private lateinit var adapter: WordlistAdapter
    private lateinit var viewModel: WordlistViewModel
    private lateinit var binding: FragmentWordlistBinding

    @Inject lateinit var interactor: DictionaryInteractor
    @Inject lateinit var schedulers: SchedulersProvider

    var clickListener = object : WordClickListener {
        override fun onClick(word: WordEntity) {
            val wordDetailsBottomDialogFragment = WordDetailsBottomDialogFragment.newInstance(word)
            wordDetailsBottomDialogFragment.show(
                (activity as AppCompatActivity).supportFragmentManager,
                WordDetailsBottomDialogFragment.TAG
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as App).appComp().inject(this)
        binding = FragmentWordlistBinding.inflate(layoutInflater)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wordlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(view.findViewById<Toolbar>(R.id.toolbar).apply {
            this.title = "Wordlist"
        })

        createAdapter(view)
        createViewModel()
        createFab(view)
        observeLiveData()
    }

    private fun createFab(view: View) {
        val fabAdd = view.findViewById<FloatingActionButton>(R.id.fab_add)
        fabAdd.setOnClickListener {
            (activity as AppCompatActivity).supportFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .add(R.id.fragment_container, TranslateFragment.newInstance(), TranslateFragment.TAG)
                .commit()
        }
    }

    private fun createViewModel() {
        viewModel = ViewModelProvider(
            this, WordlistViewModelFactory(interactor, schedulers)
        ).get(WordlistViewModel::class.java)
    }

    private fun createAdapter(view: View) {
        adapter = WordlistAdapter(emptyList(), clickListener)
        val recyclerView = view.findViewById<RecyclerView>(R.id.wordlist)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteWord(adapter.getAt(viewHolder.position).id)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun observeLiveData() {
        viewModel.getErrorLiveData().observe(viewLifecycleOwner, this::showError)
        viewModel.getProgressLiveData().observe(viewLifecycleOwner, this::showProgress)
        viewModel.getWordsLiveData().observe(viewLifecycleOwner, this::showWords)
    }

    private fun showProgress(isVisible: Boolean) {
        Log.i(TAG, "showProgress called with param = $isVisible")
        binding.progressbar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showWords(list: List<WordEntity>) {
        Log.d(TAG_ADD, "showData() called with: list = $list")
        adapter.setData(list)
    }

    private fun showError(throwable: Throwable) {
        Log.d(TAG, "showError() called with: throwable = $throwable")
        Snackbar.make(binding.root, throwable.toString(), BaseTransientBottomBar.LENGTH_SHORT).show()
    }

    companion object {
        const val TAG = "WordlistFragment"
        private const val TAG_ADD = "WordlistFragment ADD"
        private const val TAG_ERROR = "WordlistFragment ERROR"
        private const val TAG_PROGRESS = "WordlistFragment PROGRESS"
        fun newInstance(): WordlistFragment {
            return WordlistFragment()
        }
    }
}