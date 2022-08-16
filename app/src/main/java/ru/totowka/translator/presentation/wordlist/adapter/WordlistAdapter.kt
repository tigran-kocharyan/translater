package ru.totowka.translator.presentation.wordlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import ru.totowka.translator.R
import ru.totowka.translator.domain.model.WordEntity
import ru.totowka.translator.utils.Common.getPartOfSpeech
import ru.totowka.translator.utils.Common.string
import ru.totowka.translator.utils.addRipple
import ru.totowka.translator.utils.addSelection
import ru.totowka.translator.utils.callback.WordClickListener

/**
 * Адаптер для работы со списком слов из БД
 */
class WordlistAdapter(
    private var words: List<WordEntity>,
    private var clickListener: WordClickListener
) : RecyclerView.Adapter<WordViewHolder>() {
    init {
        setHasStableIds(true)
    }

    var tracker: SelectionTracker<Long>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return WordViewHolder(inflater.inflate(R.layout.word_holder, parent, false))
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        tracker?.let {
            holder.bind(words[position], clickListener, it.isSelected(position.toLong()))
        }
    }

    override fun getItemCount(): Int {
        return words.size
    }

    override fun getItemId(position: Int): Long = position.toLong()

    fun getAt(position: Int) = words[position]

    fun setData(newData: List<WordEntity>) {
        this.words = newData
        notifyDataSetChanged()
    }
}

/**
 * Holder для отображения информации о слове
 */
class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val wordView: TextView = itemView.findViewById(R.id.word)
    private val translationView: TextView = itemView.findViewById(R.id.translation)
    private val partOfSpeechView: TextView = itemView.findViewById(R.id.partOfSpeech)
    private val background: LinearLayout = itemView.findViewById(R.id.word_layout)


    fun bind(wordEntity: WordEntity, clickListener: WordClickListener, isSelected: Boolean = false) {
        wordView.text = wordEntity.text ?: itemView.context.string(R.string.undefined)
        translationView.text =
            wordEntity.meanings?.first()?.translation?.text ?: itemView.context.string(R.string.undefined)
        partOfSpeechView.text = wordEntity.meanings?.first()?.partOfSpeechCode?.let {
            getPartOfSpeech(it)
        } ?: itemView.context.string(R.string.undefined)
        itemView.setOnClickListener { clickListener.onClick(wordEntity) }

        when (isSelected) {
            true -> background.addSelection()
            false -> background.addRipple()
        }
    }

    fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
        object : ItemDetailsLookup.ItemDetails<Long>() {
            override fun getPosition(): Int = adapterPosition
            override fun getSelectionKey(): Long = itemId
        }
}