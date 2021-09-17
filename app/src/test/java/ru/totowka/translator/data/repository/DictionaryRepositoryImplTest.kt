package ru.totowka.translator.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.totowka.translator.data.datastore.db.WordsDao
import ru.totowka.translator.data.model.WordDataEntity
import ru.totowka.translator.data.model.WordDataEntity.Companion.fromEntity
import ru.totowka.translator.domain.model.WordEntity
import java.io.IOException

class DictionaryRepositoryImplTest {
    lateinit var dictionaryRepositoryImpl: DictionaryRepositoryImpl
    lateinit var wordsDao: WordsDao
    private val entityStub = WordEntity(1, "totowka", null)
    private val entityDataStub = fromEntity(entityStub)
    private val exception = IOException("")

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        wordsDao = mockk()
        dictionaryRepositoryImpl = DictionaryRepositoryImpl(wordsDao)
    }

    @Test
    fun `getAll is successful`() {
        // Arrange
        val livedata = MutableLiveData<List<WordDataEntity>>()
        livedata.value = arrayListOf(entityDataStub)
        every { wordsDao.getAllWords() } returns livedata
        val expected = arrayListOf(entityStub)

        // Act && Assert
        dictionaryRepositoryImpl.getAll().observeForever {
            Truth.assertThat(it).isEqualTo(expected)
        }
        verify(exactly = 1) { wordsDao.getAllWords() }
    }

    @Test
    fun `get is successful`() {
        // Arrange
        every { wordsDao.getWord(ID) } returns fromEntity(entityStub)
        val expected = entityStub

        // Act
        val actual = dictionaryRepositoryImpl.get(ID).blockingGet()

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
        verify(exactly = 1) { wordsDao.getWord(ID) }
    }

    @Test
    fun `get is error`() {
        // Arrange
        every { wordsDao.getWord(ID) } throws exception

        // Act
        dictionaryRepositoryImpl.get(ID).test().assertError(IOException::class.java)

        // Assert
        verify(exactly = 1) { wordsDao.getWord(ID) }
    }

    @Test
    fun `update is successful`() {
        // Arrange
        every { wordsDao.updateWord(entityDataStub) } returns Unit

        // Act
        dictionaryRepositoryImpl.update(entityStub).test().assertComplete()

        // Assert
        verify(exactly = 1) { wordsDao.updateWord(entityDataStub) }
    }

    @Test
    fun `update is error`() {
        // Arrange
        every { wordsDao.updateWord(entityDataStub) } throws exception

        // Act
        dictionaryRepositoryImpl.update(entityStub).test().assertError(IOException::class.java)

        // Assert
        verify(exactly = 1) { wordsDao.updateWord(entityDataStub) }
    }

    @Test
    fun `delete is successful`() {
        // Arrange
        every { wordsDao.deleteSingleWord(ID) } returns Unit

        // Act
        dictionaryRepositoryImpl.delete(ID).test().assertComplete()

        // Assert
        verify(exactly = 1) { wordsDao.deleteSingleWord(ID) }
    }

    @Test
    fun `delete is error`() {
        // Arrange
        every { wordsDao.deleteSingleWord(ID) } throws exception

        // Act
        dictionaryRepositoryImpl.delete(ID).test().assertError(IOException::class.java)

        // Assert
        verify(exactly = 1) { wordsDao.deleteSingleWord(ID) }
    }

    @Test
    fun `deleteAll is successful`() {
        // Arrange
        every { wordsDao.deleteAllWords() } returns Unit

        // Act
        dictionaryRepositoryImpl.deleteAll().test().assertComplete()

        // Assert
        verify(exactly = 1) { wordsDao.deleteAllWords() }
    }

    @Test
    fun `deleteAll is error`() {
        // Arrange
        every { wordsDao.deleteAllWords() } throws exception

        // Act
        dictionaryRepositoryImpl.deleteAll().test().assertError(IOException::class.java)
        // Assert
        verify(exactly = 1) { wordsDao.deleteAllWords() }
    }

    @Test
    fun `add is successful`() {
        // Arrange
        every { wordsDao.addWord(entityDataStub) } returns Unit

        // Act
        dictionaryRepositoryImpl.add(entityStub).test().assertComplete()

        // Assert
        verify(exactly = 1) { wordsDao.addWord(entityDataStub) }
    }

    @Test
    fun `add is error`() {
        // Arrange
        every { wordsDao.addWord(entityDataStub) } throws exception

        // Act
        dictionaryRepositoryImpl.add(entityStub).test().assertError(IOException::class.java)
        // Assert
        verify(exactly = 1) { wordsDao.addWord(entityDataStub) }
    }

    companion object {
        private const val ID = 0
    }
}