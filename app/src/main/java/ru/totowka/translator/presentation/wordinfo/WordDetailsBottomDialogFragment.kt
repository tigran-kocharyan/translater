package ru.totowka.translator.presentation.wordinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.totowka.translator.R
import ru.totowka.translator.databinding.FragmentWorddetailsBinding
import ru.totowka.translator.domain.model.MeaningEntity
import ru.totowka.translator.domain.model.WordEntity
import ru.totowka.translator.utils.Common.setGone


class WordDetailsBottomDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentWorddetailsBinding
    private lateinit var title: TextView
    private lateinit var meaningsRecycler: RecyclerView
    private lateinit var adapter: WordDetailsAdapter
    var wordEntity: WordEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentWorddetailsBinding.inflate(layoutInflater)
        wordEntity = arguments?.getParcelable(WORD_ENTITY_TAG)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(
            R.layout.fragment_worddetails, container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title = view.findViewById(R.id.word)
        meaningsRecycler = view.findViewById(R.id.meanings)
        setDetails()

        wordEntity?.meanings?.let {
            createAdapter(it)
        } ?: meaningsRecycler.setGone()
    }

    private fun createAdapter(meanings: ArrayList<MeaningEntity>) {
        adapter = WordDetailsAdapter(meanings)
        meaningsRecycler.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        meaningsRecycler.layoutManager = LinearLayoutManager(context)
        meaningsRecycler.adapter = adapter
    }

    private fun setDetails() {
        title.text = wordEntity?.text?.let {
             it
        } ?: "Undefined"
    }

    companion object {
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