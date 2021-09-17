package ru.totowka.translator.data.api

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.totowka.translator.data.datastore.db.RoomDictionaryDatabase
import ru.totowka.translator.data.datastore.db.WordsDao
import ru.totowka.translator.data.model.WordDataEntity
import ru.totowka.translator.domain.model.WordEntity
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class WordsDaoTest {

    private lateinit var wordsDao: WordsDao
    private lateinit var db: RoomDictionaryDatabase
    private val entityStub = WordEntity(1, "totowka", null)
    private val entityStubTwo = WordEntity(2, "totowka", null)

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, RoomDictionaryDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        wordsDao = db.wordsDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    /**
     * Эти тесты, к сожалению, не запускаются на моём устройстве, поэтому их правильность не могу проверить.
     * Но на всякий случай оставлю их здесь.
     */
    @Test
    @Throws(Exception::class)
    fun `insert`() = runBlocking {
        val expected = WordDataEntity.fromEntity(entityStub)
        wordsDao.addWord(expected)
        val actual = wordsDao.getWord(1)
        Assert.assertEquals(expected, actual)
    }

    @Test
    @Throws(Exception::class)
    fun `deleteAll`() = runBlocking {
        wordsDao.addWord(WordDataEntity.fromEntity(entityStub))
        wordsDao.addWord(WordDataEntity.fromEntity(entityStubTwo))
        wordsDao.deleteAllWords()
        wordsDao.getAllWords().value?.let { Assert.assertTrue(it.isEmpty()) }
    }

    @Test
    @Throws(Exception::class)
    fun `delete`() = runBlocking {
        wordsDao.addWord(WordDataEntity.fromEntity(entityStub))
        wordsDao.addWord(WordDataEntity.fromEntity(entityStubTwo))
        wordsDao.deleteSingleWord(1)
        Assert.assertEquals(wordsDao.getWord(2), entityStubTwo)
    }
}