package ru.totowka.translator.presentation.translate.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.totowka.translator.R
import ru.totowka.translator.domain.model.WordEntity
import ru.totowka.translator.utils.callback.WordClickListener

/**
 * Адаптер для работы со списком переведенных слов
 */
class TranslateAdapter(
    private var words: List<WordEntity>, private var clickListener: WordClickListener
) : RecyclerView.Adapter<WordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return WordViewHolder(inflater.inflate(R.layout.word_holder, parent, false))
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(words[position], clickListener)
    }

    override fun getItemCount(): Int {
        return words.size
    }

    fun setData(newData: List<WordEntity>) {
        this.words = newData
        notifyDataSetChanged()
    }
}

/**
 * Холдер для отображения слова
 */
class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val wordView: TextView = itemView.findViewById(R.id.word)
    private val translationView: TextView = itemView.findViewById(R.id.translation)
    private val partOfSpeechView: TextView = itemView.findViewById(R.id.partOfSpeech)

    fun bind(wordEntity: WordEntity, clickListener: WordClickListener) {
        itemView.setOnClickListener{clickListener.onClick(wordEntity)}
        wordView.text = wordEntity.text ?: ""
        translationView.text = wordEntity.meanings?.first()?.translation?.text ?: ""
        partOfSpeechView.text = wordEntity.meanings?.first()?.partOfSpeechCode ?: ""
    }
}