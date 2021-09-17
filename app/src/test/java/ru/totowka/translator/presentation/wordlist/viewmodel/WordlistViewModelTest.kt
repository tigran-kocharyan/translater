package ru.totowka.translator.presentation.wordlist.viewmodel

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
import ru.totowka.translator.domain.model.WordEntity
import ru.totowka.translator.utils.scheduler.SchedulersProvider
import ru.totowka.translator.utils.scheduler.SchedulersProviderImplStub
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class WordlistViewModelTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    lateinit var viewModel: WordlistViewModel
    lateinit var dictionaryInteractor: DictionaryInteractor
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
        schedulers = SchedulersProviderImplStub()

        val livedata = MutableLiveData<List<WordEntity>>()
        livedata.value = arrayListOf(entityStub)
        every { dictionaryInteractor.getWords() } returns livedata
        every { wordsObserver.onChanged(any()) } just Runs
        every { progressObserver.onChanged(any()) } just Runs
        every { errorObserver.onChanged(any()) } just Runs

        viewModel = WordlistViewModel(dictionaryInteractor, schedulers)
        viewModel.getWordsLiveData().observeForever(wordsObserver)
        viewModel.getErrorLiveData().observeForever(errorObserver)
        viewModel.getProgressLiveData().observeForever(progressObserver)
    }

    @Test
    fun `getWord is success`() {
        every { dictionaryInteractor.getWord(ID) } returns Single.just(entityStub)

        viewModel.getWord(ID)

        verifySequence {
            wordsObserver.onChanged(listOf(entityStub))
            progressObserver.onChanged(true)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { dictionaryInteractor.getWord(ID) }
        verify { errorObserver wasNot Called }
    }

    @Test
    fun `getWord is error`() {
        every { dictionaryInteractor.getWord(ID) } returns Single.error(exception)

        viewModel.getWord(ID)

        verifySequence {
            wordsObserver.onChanged(listOf(entityStub))
            progressObserver.onChanged(true)
            errorObserver.onChanged(exception)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { dictionaryInteractor.getWord(ID) }
    }


    @Test
    fun `deleteWord is success`() {
        every { dictionaryInteractor.deleteWord(ID) } returns Completable.complete()

        viewModel.deleteWord(ID)

        verify { errorObserver wasNot Called }
    }

    @Test
    fun `deleteWord is error`() {
        every { dictionaryInteractor.deleteWord(ID) } returns Completable.error(exception)

        viewModel.deleteWord(ID)

        verifySequence {
            progressObserver.onChanged(true)
            errorObserver.onChanged(exception)
            progressObserver.onChanged(false)
        }
    }


    @Test
    fun `deleteAllWords is success`() {
        every { dictionaryInteractor.deleteAllWords() } returns Completable.complete()

        viewModel.deleteAllWords()

        verify { errorObserver wasNot Called }
    }

    @Test
    fun `deleteAllWords is error`() {
        every { dictionaryInteractor.deleteAllWords() } returns Completable.error(exception)

        viewModel.deleteAllWords()

        verifySequence {
            progressObserver.onChanged(true)
            errorObserver.onChanged(exception)
            progressObserver.onChanged(false)
        }
    }

    @Test
    fun `addWord is success`() {
        every { dictionaryInteractor.addWord(entityStub) } returns Completable.complete()

        viewModel.addWord(entityStub)

        verifySequence {
            progressObserver.onChanged(true)
        }

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
    }

    @Test
    fun `updateWord is success`() {
        every { dictionaryInteractor.updateWord(entityStub) } returns Completable.complete()

        viewModel.updateWord(entityStub)

        verify { errorObserver wasNot Called }
    }

    @Test
    fun `updateWord is error`() {
        every { dictionaryInteractor.updateWord(entityStub) } returns Completable.error(exception)

        viewModel.updateWord(entityStub)

        verify(exactly = 1) { errorObserver.onChanged(exception) }
    }

    companion object {
        private const val ID = 0
    }
}