package ru.totowka.translator.presentation.wordlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.totowka.translator.R
import ru.totowka.translator.domain.model.WordEntity


class WordlistAdapter(private var words: List<WordEntity>
) : RecyclerView.Adapter<WordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return WordViewHolder(inflater.inflate(R.layout.word_holder, parent, false))
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(words[position])
    }

    override fun getItemCount(): Int {
        return words.size
    }

    fun getAt(position: Int)  = words[position]

    fun setData(newData: List<WordEntity>) {
        this.words = newData
        notifyDataSetChanged()
    }
}

class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val wordView: TextView = itemView.findViewById(R.id.word)
    private val translationView: TextView = itemView.findViewById(R.id.translation)
    private val partOfSpeechView: TextView = itemView.findViewById(R.id.partOfSpeech)

    fun bind(wordEntity: WordEntity) {
        wordView.text = wordEntity.text ?: ""
        translationView.text = wordEntity.meanings?.first()?.translation?.text ?: ""
        partOfSpeechView.text = wordEntity.meanings?.first()?.partOfSpeechCode ?: ""
    }
}

