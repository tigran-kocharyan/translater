package ru.totowka.translator.domain.interactor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.totowka.translator.data.repository.DictionaryRepositoryImpl
import ru.totowka.translator.domain.model.WordEntity
import java.io.IOException


class DictionaryInteractorTest {
    lateinit var dictionaryRepositoryImpl: DictionaryRepositoryImpl
    lateinit var dictionaryInteractor: DictionaryInteractor
    private val entityStub = WordEntity(1, "totowka", null)

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        dictionaryRepositoryImpl = mockk()
        dictionaryInteractor = DictionaryInteractor(dictionaryRepositoryImpl)
    }

    @Test
    fun `addWord is successful`() {
        // Arrange
        every { dictionaryRepositoryImpl.add(entityStub) } returns Completable.fromCallable { }

        // Assert
        dictionaryInteractor.addWord(entityStub).test().assertComplete()
        verify(exactly = 1) { dictionaryRepositoryImpl.add(entityStub) }
    }

    @Test
    fun `addWord is error`() {
        // Arrange
        every { dictionaryRepositoryImpl.add(entityStub) } returns Completable.fromCallable { throw IOException() }

        // Assert
        dictionaryInteractor.addWord(entityStub).test().assertError(IOException::class.java)
        verify(exactly = 1) { dictionaryRepositoryImpl.add(entityStub) }
    }

    @Test
    fun `updateWord is successful`() {
        // Arrange
        every { dictionaryRepositoryImpl.update(entityStub) } returns Completable.fromCallable { }

        // Assert
        dictionaryInteractor.updateWord(entityStub).test().assertComplete()
        verify(exactly = 1) { dictionaryRepositoryImpl.update(entityStub) }
    }

    @Test
    fun `updateWord is error`() {
        // Arrange
        every { dictionaryRepositoryImpl.update(entityStub) } returns Completable.fromCallable { throw IOException() }

        // Assert
        dictionaryInteractor.updateWord(entityStub).test().assertError(IOException::class.java)
        verify(exactly = 1) { dictionaryRepositoryImpl.update(entityStub) }
    }

    @Test
    fun `deleteAllWords is successful`() {
        // Arrange
        every { dictionaryRepositoryImpl.deleteAll() } returns Completable.fromCallable { }

        // Assert
        dictionaryInteractor.deleteAllWords().test().assertComplete()
        verify(exactly = 1) { dictionaryRepositoryImpl.deleteAll() }
    }

    @Test
    fun `deleteAllWords is error`() {
        // Arrange
        every { dictionaryRepositoryImpl.deleteAll() } returns Completable.fromCallable { throw IOException() }

        // Assert
        dictionaryInteractor.deleteAllWords().test().assertError(IOException::class.java)
        verify(exactly = 1) { dictionaryRepositoryImpl.deleteAll() }
    }

    @Test
    fun `deleteWord is successful`() {
        // Arrange
        every { dictionaryRepositoryImpl.delete(ID) } returns Completable.fromCallable { }

        // Assert
        dictionaryInteractor.deleteWord(ID).test().assertComplete()
        verify(exactly = 1) { dictionaryRepositoryImpl.delete(ID) }
    }

    @Test
    fun `deleteWord is error`() {
        // Arrange
        every { dictionaryRepositoryImpl.delete(ID) } returns Completable.fromCallable { throw IOException() }

        // Assert
        dictionaryInteractor.deleteWord(ID).test().assertError(IOException::class.java)
        verify(exactly = 1) { dictionaryRepositoryImpl.delete(ID) }
    }

    @Test
    fun `getWord is successful`() {
        // Arrange
        every { dictionaryRepositoryImpl.get(ID) } returns Single.just(entityStub)
        val expected = entityStub

        // Act
        val actual = dictionaryInteractor.getWord(ID).blockingGet()

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
        verify(exactly = 1) { dictionaryRepositoryImpl.get(ID) }
    }

    @Test
    fun `getWord is error`() {
        // Arrange
        every { dictionaryRepositoryImpl.get(ID) } returns Single.fromCallable { throw IOException() }
        val expected = entityStub

        // Act
        val actual = dictionaryInteractor.getWord(ID).test().assertError(IOException::class.java)
        verify(exactly = 1) { dictionaryRepositoryImpl.get(ID) }
    }

    @Test
    fun `getWords is successful`() {
        // Arrange
        val livedata = MutableLiveData<List<WordEntity>>()
        livedata.value = arrayListOf(entityStub)
        every { dictionaryRepositoryImpl.getAll() } returns livedata
        val expected = arrayListOf(entityStub)

        // Act
        val actual = dictionaryInteractor.getWords()

        // Assert
        Truth.assertThat(actual.value).isEqualTo(expected)
        verify(exactly = 1) { dictionaryRepositoryImpl.getAll() }
    }

    companion object {
        private const val ID = 0
    }
}