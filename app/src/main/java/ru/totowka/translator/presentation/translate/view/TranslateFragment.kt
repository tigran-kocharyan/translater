package ru.totowka.translator.presentation.translate.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.CompositeDisposable
import ru.totowka.translator.App
import ru.totowka.translator.R
import ru.totowka.translator.databinding.FragmentTranslateBinding
import ru.totowka.translator.domain.interactor.DictionaryInteractor
import ru.totowka.translator.domain.interactor.TranslationInteractor
import ru.totowka.translator.domain.model.WordEntity
import ru.totowka.translator.presentation.translate.adapter.TranslateAdapter
import ru.totowka.translator.presentation.translate.viewmodel.TranslateViewModel
import ru.totowka.translator.presentation.translate.viewmodel.TranslateViewModelFactory
import ru.totowka.translator.utils.SchedulersProvider
import ru.totowka.translator.utils.callback.WordClickListener
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class TranslateFragment : Fragment() {
    private lateinit var viewModel: TranslateViewModel
    private lateinit var binding: FragmentTranslateBinding
    private lateinit var adapter: TranslateAdapter
    private val disposables = CompositeDisposable()

    var clickListener = object : WordClickListener {
        override fun onClick(word: WordEntity) {
            viewModel.addWord(word)
            exit()
        }
    }

    @Inject
    lateinit var dictionaryInteractor: DictionaryInteractor

    @Inject
    lateinit var translationInteractor: TranslationInteractor

    @Inject
    lateinit var schedulers: SchedulersProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as App).appComp().inject(this)
        binding = FragmentTranslateBinding.inflate(layoutInflater)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_translate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        supportBackIcon(view)
        createAdapter(view)
        createViewModel()
        observeLiveData()

        disposables.add(Observable.create(ObservableOnSubscribe<String> { subscriber ->
            view.findViewById<TextInputLayout>(R.id.word_input).editText?.doOnTextChanged { input, _, _, _ ->
                subscriber.onNext(input.toString())
            }
        })
            .map { text -> text.trim() }
            .debounce(250, TimeUnit.MILLISECONDS)
            .filter { text -> text.isNotBlank() }
            .distinctUntilChanged()
            .subscribe { text ->
                viewModel.translate(text)
            })
    }

    private fun createViewModel() {
        viewModel = ViewModelProvider(
            this, TranslateViewModelFactory(dictionaryInteractor, translationInteractor, schedulers)
        ).get(TranslateViewModel::class.java)
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

    private fun createAdapter(view: View) {
        adapter = TranslateAdapter(emptyList(), clickListener)
        val recyclerView = view.findViewById<RecyclerView>(R.id.wordlist)
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun supportBackIcon(view: View) {
        this.setHasOptionsMenu(true)
        (activity as AppCompatActivity).apply {
            this.setSupportActionBar(view.findViewById(R.id.toolbar))
            this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            this.supportActionBar?.setDisplayShowHomeEnabled(true)
            this.supportActionBar?.title = "Translate"
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

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
        disposables.clear()
    }

    companion object {
        const val TAG = "TranslateFragment"
        private const val TAG_ADD = "$TAG ADD"
        private const val TAG_ERROR = "$TAG ERROR"
        private const val TAG_PROGRESS = "$TAG PROGRESS"
        fun newInstance(): TranslateFragment {
            return TranslateFragment()
        }
    }
}