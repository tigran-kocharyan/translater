package ru.totowka.translator.presentation.wordinfo

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import ru.totowka.translator.R
import ru.totowka.translator.domain.model.MeaningEntity
import ru.totowka.translator.utils.Common.getPartOfSpeech
import ru.totowka.translator.utils.Common.setGone
import java.net.URI

class WordDetailsAdapter(
    private var words: List<MeaningEntity>
) : RecyclerView.Adapter<MeaningViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeaningViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MeaningViewHolder(inflater.inflate(R.layout.meaning_holder, parent, false))
    }

    override fun onBindViewHolder(holder: MeaningViewHolder, position: Int) {
        holder.bind(words[position])
    }

    override fun getItemCount(): Int {
        return words.size
    }
}

class MeaningViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val imageView: ImageView = itemView.findViewById(R.id.image)
    private val translationView: TextView = itemView.findViewById(R.id.translation)
    private val noteView: TextView = itemView.findViewById(R.id.note)
    private val transcriptionView: TextView = itemView.findViewById(R.id.transcription)
    private val partOfSpeechView: TextView = itemView.findViewById(R.id.partOfSpeech)

    fun bind(meaningEntity: MeaningEntity) {
        meaningEntity.imageUrl?.let {
            var url = URI(it).host + URI(it).path
            if (!url.startsWith("https://") && !url.startsWith("http://"))
                url = "https://$url"
//            Glide
//                .with(this)
//                .load(url)
//                .centerCrop()
//                .placeholder(R.drawable.ic_image)
//                .error(R.drawable.ic_broken)
//                .fallback(R.drawable.ic_broken)
//                .into(image)
            imageView.load(url) {
                crossfade(true)
                error(R.drawable.ic_broken)
                fallback(R.drawable.ic_broken)
                placeholder(R.drawable.ic_image)
                transformations(CircleCropTransformation())
                scale(Scale.FIT)
            }
        } ?: imageView.setGone()
        meaningEntity.translation?.let { translation ->
            translationView.text = translation.text
            translation.note?.let {
                noteView.text = it
            } ?: noteView.setGone()
        } ?: noteView.setGone()
        meaningEntity.transcription?.let {
            transcriptionView.text = it
        } ?: transcriptionView.setGone()
        meaningEntity.partOfSpeechCode?.let {
            partOfSpeechView.text = getPartOfSpeech(it)
        } ?: partOfSpeechView.setGone()
    }
}