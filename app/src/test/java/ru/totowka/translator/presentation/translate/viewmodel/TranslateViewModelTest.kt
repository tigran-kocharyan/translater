package ru.totowka.translator.presentation.translate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ru.totowka.translator.data.model.WordDataEntity
import ru.totowka.translator.domain.interactor.DictionaryInteractor
import ru.totowka.translator.domain.interactor.TranslationInteractor
import ru.totowka.translator.domain.model.WordEntity
import ru.totowka.translator.utils.scheduler.SchedulersProvider
import ru.totowka.translator.utils.scheduler.SchedulersProviderImplStub
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class TranslateViewModelTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    lateinit var viewModel: TranslateViewModel
    lateinit var dictionaryInteractor: DictionaryInteractor
    lateinit var translationInteractor: TranslationInteractor
    lateinit var schedulers: SchedulersProvider
    private val entityStub = WordEntity(1, "totowka", null)
    private val entityDataStub = WordDataEntity.fromEntity(entityStub)
    private val exception = IOException("")

    var wordsObserver: Observer<List<WordEntity>> = mockk()
    var progressObserver: Observer<Boolean> = mockk()
    var errorObserver: Observer<Throwable> = mockk()

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        dictionaryInteractor = mockk()
        translationInteractor = mockk()
        schedulers = SchedulersProviderImplStub()

        val livedata = MutableLiveData<List<WordEntity>>()
        livedata.value = arrayListOf(entityStub)
        every { dictionaryInteractor.getWords() } returns livedata
        every { wordsObserver.onChanged(any()) } just Runs
        every { progressObserver.onChanged(any()) } just Runs
        every { errorObserver.onChanged(any()) } just Runs

        viewModel = TranslateViewModel(dictionaryInteractor, translationInteractor, schedulers)
        viewModel.getWordsLiveData().observeForever(wordsObserver)
        viewModel.getErrorLiveData().observeForever(errorObserver)
        viewModel.getProgressLiveData().observeForever(progressObserver)
    }

    @Test
    fun `translate is success`() {
        every { translationInteractor.getTranslation(WORD) } returns Single.just(listOf(entityStub))

        viewModel.translate(WORD)

        verifySequence {
            progressObserver.onChanged(true)
            wordsObserver.onChanged(listOf(entityStub))
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { translationInteractor.getTranslation(WORD) }
        verify { errorObserver wasNot Called }
    }

    @Test
    fun `translate is error`() {
        every { translationInteractor.getTranslation(WORD) } returns Single.error(exception)

        viewModel.translate(WORD)

        verifySequence {
            progressObserver.onChanged(true)
            errorObserver.onChanged(exception)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { translationInteractor.getTranslation(WORD) }
    }

    @Test
    fun `addWord is success`() {
        every { dictionaryInteractor.addWord(entityStub) } returns Completable.complete()

        viewModel.addWord(entityStub)

        verifySequence {
            progressObserver.onChanged(true)
        }
        verify(exactly = 1) { dictionaryInteractor.addWord(entityStub) }
        verify { errorObserver wasNot Called }
    }

    @Test
    fun `addWord is error`() {
        every { dictionaryInteractor.addWord(entityStub) } returns Completable.error(exception)

        viewModel.addWord(entityStub)

        verifySequence {
            progressObserver.onChanged(true)
            errorObserver.onChanged(exception)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { dictionaryInteractor.addWord(entityStub) }
    }

    companion object {
        private const val ID = 0
        private const val WORD = "WORD"
    }
}