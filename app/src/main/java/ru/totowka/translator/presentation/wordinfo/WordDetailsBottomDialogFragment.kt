package ru.totowka.translator.presentation.wordinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.totowka.translator.R
import ru.totowka.translator.databinding.FragmentWorddetailsBinding
import ru.totowka.translator.domain.model.MeaningEntity
import ru.totowka.translator.domain.model.WordEntity
import ru.totowka.translator.utils.Common.setGone
import ru.totowka.translator.utils.Common.string

/**
 * BottomSheetDialogFragment для отображения смыслов переведенного слова
 */
class WordDetailsBottomDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentWorddetailsBinding
    private lateinit var adapter: WordDetailsAdapter
    var wordEntity: WordEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentWorddetailsBinding.inflate(layoutInflater)
        wordEntity = arguments?.getParcelable(WORD_ENTITY_TAG)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWorddetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDetails()
        wordEntity?.meanings?.let {
            createAdapter(it)
        } ?: binding.meanings.setGone()
    }

    private fun createAdapter(meanings: ArrayList<MeaningEntity>) {
        adapter = WordDetailsAdapter(meanings)
        binding.meanings.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        binding.meanings.layoutManager = LinearLayoutManager(context)
        binding.meanings.adapter = adapter
    }

    private fun setDetails() {
        binding.word.text = wordEntity?.text ?: context?.string(R.string.undefined)
    }

    companion object {
        /**
         * Получение инстанса [WordDetailsBottomDialogFragment]
         */
        fun newInstance(wordEntity: WordEntity) =
            WordDetailsBottomDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(WORD_ENTITY_TAG, wordEntity)
                }
            }

        const val TAG = "WordDetailsBottomDialog"
        const val WORD_ENTITY_TAG = "WORD_ENTITY_TAG"
    }
}