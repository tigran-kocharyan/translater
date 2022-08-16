package ru.totowka.translator.presentation.learn.view

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import ru.totowka.translator.R
import ru.totowka.translator.databinding.FragmentExplanationBinding
import ru.totowka.translator.databinding.FragmentLearnBinding
import ru.totowka.translator.domain.model.LearnEntity
import ru.totowka.translator.domain.model.LearnEntity.Companion.fromEntity
import ru.totowka.translator.domain.model.MeaningEntity
import ru.totowka.translator.domain.model.WordEntity
import ru.totowka.translator.presentation.SharedViewModel
import ru.totowka.translator.presentation.learn.adapter.LearnAdapter
import ru.totowka.translator.presentation.wordinfo.WordDetailsAdapter
import ru.totowka.translator.presentation.wordinfo.WordDetailsBottomDialogFragment
import ru.totowka.translator.presentation.wordlist.adapter.WordlistAdapter
import ru.totowka.translator.utils.Common
import ru.totowka.translator.utils.Common.setGone
import ru.totowka.translator.utils.Common.string
import ru.totowka.translator.utils.callback.SwipeToDeleteCallback
import ru.totowka.translator.utils.callback.WordClickListener
import java.io.IOException
import java.net.URI

/**
 * Фрагмент, отвечающий за экран изучения слов.
 */
class ExplanationFragment : Fragment() {
    private lateinit var binding: FragmentExplanationBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var adapter: WordDetailsAdapter
    var learn: LearnEntity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExplanationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        supportBackIcon(view)
        binding.fabNext.setOnClickListener {
            findNavController().navigate(R.id.action_explanationFragment_to_translateToEnglishFragment)
        }
        learn = sharedViewModel.learn?.value
        showWord()
        createAdapter()
    }

    private fun supportBackIcon(view: View) {
        this.setHasOptionsMenu(true)
        (activity as AppCompatActivity).apply {
            this.setSupportActionBar(view.findViewById(R.id.learn_toolbar))
            this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            this.supportActionBar?.setDisplayShowHomeEnabled(true)
            this.supportActionBar?.title = string(R.string.explanation)
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

    private var player: MediaPlayer? = null

    /**
     * Метод для проигрывания аудио
     */
    @Synchronized
    private fun playSound(soundUrl: String?) {
        if (Common.isConnectedInternet(requireContext())) {
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
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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

    private fun showWord() {
        val meaning = learn?.word?.meanings?.first() ?: MeaningEntity()
        binding.word.text = learn?.word?.text
        meaning.imageUrl?.let {
            var url = URI(it).host + URI(it).path
            if (!url.startsWith("https://") && !url.startsWith("http://"))
                url = "https://$url"
            binding.image.load(url) {
                crossfade(true)
                error(R.drawable.ic_broken)
                fallback(R.drawable.ic_broken)
                placeholder(R.drawable.ic_image)
                transformations(CircleCropTransformation())
                scale(Scale.FIT)
            }
        } ?: binding.image.setGone()
        meaning.translation?.let { translation ->
            binding.translation.text = translation.text
            translation.note?.let {
                binding.note.text = it
            } ?: binding.note.setGone()
        } ?: binding.note.setGone()
        meaning.transcription?.let {
            binding.transcription.text = "[$it]"
        } ?: binding.transcription.setGone()
        meaning.partOfSpeechCode?.let {
            binding.partOfSpeech.text = Common.getPartOfSpeech(it)
        } ?: binding.partOfSpeech.setGone()
        meaning.soundUrl?.let { soundUrl ->
            var url = soundUrl
            if (!url.startsWith("https://") && !url.startsWith("http://"))
                url = "https:$soundUrl"
            binding.soundplayer.setOnClickListener { playSound(url) }
        } ?: binding.partOfSpeech.setGone()
        if (learn?.word?.meanings?.size == 1)
            binding.secondary.setGone()
    }

    private fun createAdapter() {
        adapter = WordDetailsAdapter(learn?.word?.meanings?.drop(1) ?: emptyList())
        binding.otherMeanings.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        binding.otherMeanings.layoutManager = LinearLayoutManager(requireContext())
        binding.otherMeanings.adapter = adapter
    }

    companion object {
        const val TAG = "ExplanationFragment"
        private const val TAG_ADD = "$TAG ADD"
        private const val TAG_ERROR = "$TAG ERROR"
        private const val TAG_PROGRESS = "$TAG PROGRESS"

        /**
         * Получение объекта [ExplanationFragment]
         */
        fun newInstance() = ExplanationFragment()
    }
}