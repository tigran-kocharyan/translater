package ru.totowka.translator.presentation.learn.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.NonDisposableHandle.parent
import ru.totowka.translator.R
import ru.totowka.translator.databinding.LearnWordHolderBinding
import ru.totowka.translator.domain.model.LearnEntity
import ru.totowka.translator.domain.model.WordEntity
import ru.totowka.translator.presentation.wordlist.adapter.WordViewHolder
import ru.totowka.translator.utils.Common.getPartOfSpeech
import ru.totowka.translator.utils.Common.string
import ru.totowka.translator.utils.addRipple
import ru.totowka.translator.utils.addSelection
import ru.totowka.translator.utils.callback.LearnWordClickListener
import ru.totowka.translator.utils.callback.WordClickListener

/**
 * Адаптер для работы со списком слов для изучения
 */
class LearnAdapter(
    private var words: List<LearnEntity>,
    private var clickListener: LearnWordClickListener
) : RecyclerView.Adapter<LearnWordViewHolder>() {
    init {
        setHasStableIds(true)
    }

    private lateinit var binding: LearnWordHolderBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LearnWordViewHolder {
        binding = LearnWordHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LearnWordViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: LearnWordViewHolder, position: Int) {
        holder.bind(words[position])
    }

    override fun getItemCount(): Int {
        return words.size
    }

    override fun getItemId(position: Int): Long = position.toLong()

    fun getAt(position: Int) = words[position]

    fun setData(newData: List<LearnEntity>) {
        this.words = newData
        notifyDataSetChanged()
    }
}

/**
 * Holder для отображения информации о слове
 */
class LearnWordViewHolder(
    private val binding: LearnWordHolderBinding,
    private val listener: LearnWordClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(learn: LearnEntity) {
        binding.word.text = learn.word.text ?: itemView.context.string(R.string.undefined)
        binding.translation.text = learn.word.meanings?.first()?.translation?.text
            ?: itemView.context.string(R.string.undefined)
        binding.root.setOnClickListener {
            if (!learn.state.isLearned)
                listener.onClick(learn)
        }
        updateLearnState(learn.state.isLearned)
    }

    private fun updateLearnState(state: Boolean) {
        when (state) {
            true -> binding.learnState.setImageResource(R.drawable.ic_success)
            false -> binding.learnState.setImageResource(R.drawable.ic_next)
        }
    }
}