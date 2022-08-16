package ru.totowka.translator.presentation.wordlist.view

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import ru.totowka.translator.App
import ru.totowka.translator.R
import ru.totowka.translator.databinding.FragmentWordlistBinding
import ru.totowka.translator.domain.interactor.DictionaryInteractor
import ru.totowka.translator.domain.model.LearnEntity
import ru.totowka.translator.domain.model.WordEntity
import ru.totowka.translator.presentation.LauncherActivity
import ru.totowka.translator.presentation.SharedViewModel
import ru.totowka.translator.presentation.learn.view.LearnFragment
import ru.totowka.translator.presentation.translate.view.TranslateFragment
import ru.totowka.translator.presentation.wordinfo.WordDetailsBottomDialogFragment
import ru.totowka.translator.presentation.wordlist.adapter.WordlistAdapter
import ru.totowka.translator.presentation.wordlist.adapter.WordlistKeyProvider
import ru.totowka.translator.presentation.wordlist.adapter.WordlistLookup
import ru.totowka.translator.presentation.wordlist.viewmodel.WordlistViewModel
import ru.totowka.translator.presentation.wordlist.viewmodel.WordlistViewModelFactory
import ru.totowka.translator.utils.Common.string
import ru.totowka.translator.utils.callback.SwipeToDeleteCallback
import ru.totowka.translator.utils.callback.WordClickListener
import ru.totowka.translator.utils.scheduler.SchedulersProvider
import javax.inject.Inject

/**
 * Фрагмент со списком слов
 */
class WordlistFragment : Fragment(), ActionMode.Callback {
    private var actionMode: ActionMode? = null
    private lateinit var adapter: WordlistAdapter
    private lateinit var viewModel: WordlistViewModel
    private lateinit var binding: FragmentWordlistBinding
    private lateinit var tracker: SelectionTracker<Long>
    private val sharedViewModel: SharedViewModel by activityViewModels()

    @Inject
    lateinit var interactor: DictionaryInteractor

    @Inject
    lateinit var schedulers: SchedulersProvider

    private var clickListener = object : WordClickListener {
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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWordlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar.apply {
            this.title = context.string(R.string.wordlist_name)
        })
        setHasOptionsMenu(true)
        createAdapter()
        createViewModel()
        createFab()
        observeLiveData()
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.main, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?) = true

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.learn -> {
                if (tracker.selection!!.size() > 0) {
                    val result = tracker.selection!!.map {
                        adapter.getAt(it.toInt())
                    }.toList()
                    tracker.clearSelection()
                    sharedViewModel.setLearns(result.map { LearnEntity(it) })
                    (activity as AppCompatActivity).supportFragmentManager
                        .beginTransaction()
                        .addToBackStack(null)
                        .add(R.id.fragment_container, LearnFragment.newInstance(), LearnFragment.TAG)
                        .commit()
                } else {
                    Toast.makeText(this.context, "Nothing to learn!", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        tracker.clearSelection()
        actionMode = null
        val adapter = (binding.wordlist.adapter as WordlistAdapter)
        binding.wordlist.adapter = adapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        tracker.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        tracker.onRestoreInstanceState(savedInstanceState)
        if (tracker.hasSelection()) {
            actionMode = (activity as LauncherActivity).startSupportActionMode(this@WordlistFragment)
            actionMode?.title = getString(R.string.learn, tracker.selection.size())
        }
        super.onViewStateRestored(savedInstanceState)
    }

    private fun createFab() {
        binding.fabAdd.setOnClickListener {
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

    private fun createAdapter() {
        adapter = WordlistAdapter(emptyList(), clickListener)
        binding.wordlist.layoutManager = LinearLayoutManager(context)
        binding.wordlist.adapter = adapter
        binding.wordlist.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        adapter.tracker = createTracker()

        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteWord(adapter.getAt(viewHolder.position).id)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.wordlist)
    }

    private fun createTracker(): SelectionTracker<Long> {
        tracker = SelectionTracker.Builder(
            "wordlistSelection",
            binding.wordlist,
            WordlistKeyProvider(binding.wordlist),
            WordlistLookup(binding.wordlist),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()
        tracker.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    if (actionMode == null) {
                        val currentActivity = activity as LauncherActivity
                        actionMode = currentActivity.startSupportActionMode(this@WordlistFragment)
                    }
                    if (tracker.selection.size() > 0) {
                        actionMode?.title = getString(R.string.learn, tracker.selection.size())
                    } else {
                        actionMode?.finish()
                    }
                }
            })
        return tracker
    }

    private fun observeLiveData() {
        viewModel.getErrorLiveData().observe(viewLifecycleOwner, this::showError)
        viewModel.getProgressLiveData().observe(viewLifecycleOwner, this::showProgress)
        viewModel.getWordsLiveData().observe(viewLifecycleOwner, this::showWords)
    }

    private fun showProgress(isVisible: Boolean) {
        Log.i(TAG, "showProgress called with param = $isVisible")
        binding.progressbar.visibility = if (isVisible) VISIBLE else View.GONE
    }

    private fun showWords(list: List<WordEntity>) {
        Log.d(TAG_ADD, "showData() called with: list = $list")
        binding.addWordTip.visibility = if (list.isEmpty()) VISIBLE else INVISIBLE
        adapter.setData(list)
    }

    private fun showError(throwable: Throwable) {
        Log.d(TAG, "showError() called with: throwable = $throwable")
        Snackbar.make(binding.wordlistRoot, throwable.toString(), BaseTransientBottomBar.LENGTH_SHORT).show()
    }

    companion object {
        const val TAG = "WordlistFragment"
        private const val TAG_ADD = "WordlistFragment ADD"
        private const val TAG_ERROR = "WordlistFragment ERROR"
        private const val TAG_PROGRESS = "WordlistFragment PROGRESS"

        /**
         * Получение объекта [WordlistFragment]
         */
        fun newInstance(): WordlistFragment {
            return WordlistFragment()
        }
    }
}