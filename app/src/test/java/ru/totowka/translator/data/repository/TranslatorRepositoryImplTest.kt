package ru.totowka.translator.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.totowka.translator.data.api.SkyEngApi
import ru.totowka.translator.data.model.WordDataEntity
import ru.totowka.translator.domain.model.WordEntity
import java.io.IOException

class TranslatorRepositoryImplTest {
    lateinit var translatorRepositoryImpl: TranslatorRepositoryImpl
    lateinit var skyEngApi: SkyEngApi
    private val entityStub = WordEntity(1, "totowka", null)
    private val entityDataStub = WordDataEntity.fromEntity(entityStub)
    private val exception = IOException("")

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        skyEngApi = mockk()
        translatorRepositoryImpl = TranslatorRepositoryImpl(skyEngApi)
    }

    @Test
    fun `getWordTranslation is successful`() {
        // Arrange
        every { skyEngApi.getWordTranslation(WORD) } returns Single.just(listOf(entityDataStub))
        val expected = listOf(entityStub)

        // Act
        val actual = translatorRepositoryImpl.getWordTranslation(WORD).blockingGet()

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
        verify(exactly = 1) { skyEngApi.getWordTranslation(WORD) }
    }

    @Test
    fun `getWordTranslation is error`() {
        // Arrange
        every { skyEngApi.getWordTranslation(WORD) } throws exception

        // Act && Assert
        assertThrows(IOException::class.java) { translatorRepositoryImpl.getWordTranslation(WORD) }
        verify(exactly = 1) { skyEngApi.getWordTranslation(WORD) }
    }


    companion object {
        private const val ID = 0
        private const val WORD = "WORD"
    }
}