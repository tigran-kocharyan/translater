package ru.totowka.translator.presentation.wordinfo

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import ru.totowka.translator.R
import ru.totowka.translator.domain.model.MeaningEntity
import ru.totowka.translator.utils.Common.getPartOfSpeech
import ru.totowka.translator.utils.Common.isConnectedInternet
import ru.totowka.translator.utils.Common.setGone
import java.io.IOException
import java.net.URI

/**
 * Адаптер для отображения слов в BottomSheet
 */
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

/**
 * Holder для отображения смысла слова
 */
class MeaningViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var player: MediaPlayer? = null

    /**
     * Метод для проигрывания аудио
     */
    @Synchronized
    private fun playSound(soundUrl: String?) {
        if (isConnectedInternet(itemView.context)) {
            releasePlayer(player)
            try {
                player = MediaPlayer()
                player!!.setOnPreparedListener { preparedPlayer: MediaPlayer ->
                    if (player != null) {
                        try {
                            preparedPlayer.start()
                        } catch (e: RuntimeException) {
                        }
                    }
                }
                player!!.setOnErrorListener { mediaPlayer: MediaPlayer?, _, _ ->
                    showError("Error while playing sound!")
                    releasePlayer(mediaPlayer)
                    true
                }
                player!!.setOnCompletionListener { mediaPlayer: MediaPlayer ->
                    if (mediaPlayer.isPlaying) {
                        mediaPlayer.stop()
                    }
                    releasePlayer(mediaPlayer)
                }
                player!!.setDataSource(soundUrl)
                player!!.prepareAsync()
            } catch (e: IOException) {
            }
        } else {
            showError("Connect to the Internet!")
        }
    }

    /**
     * Отображение ошибки
     */
    private fun showError(message: String) {
        Toast.makeText(itemView.context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Метод для остановки аудио
     */
    @Synchronized
    private fun releasePlayer(mediaPlayer: MediaPlayer?) {
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                }
                mediaPlayer.release()
            } catch (e: RuntimeException) {
            }
        }
    }

    private val imageView: ImageView = itemView.findViewById(R.id.image)
    private val soundPlayerButton: ImageButton = itemView.findViewById(R.id.soundplayer)
    private val translationView: TextView = itemView.findViewById(R.id.translation)
    private val noteView: TextView = itemView.findViewById(R.id.note)
    private val transcriptionView: TextView = itemView.findViewById(R.id.transcription)
    private val partOfSpeechView: TextView = itemView.findViewById(R.id.partOfSpeech)

    fun bind(meaningEntity: MeaningEntity) {
        meaningEntity.imageUrl?.let {
            var url = URI(it).host + URI(it).path
            if (!url.startsWith("https://") && !url.startsWith("http://"))
                url = "https://$url"
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
            transcriptionView.text = "[$it]"
        } ?: transcriptionView.setGone()
        meaningEntity.partOfSpeechCode?.let {
            partOfSpeechView.text = getPartOfSpeech(it)
        } ?: partOfSpeechView.setGone()
        meaningEntity.soundUrl?.let { soundUrl ->
            var url = soundUrl
            if (!url.startsWith("https://") && !url.startsWith("http://"))
                url = "https:$soundUrl"
            soundPlayerButton.setOnClickListener { playSound(url) }
        } ?: partOfSpeechView.setGone()
    }
}