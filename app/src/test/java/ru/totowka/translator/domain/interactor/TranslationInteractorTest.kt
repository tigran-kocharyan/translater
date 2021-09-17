package ru.totowka.translator.domain.interactor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.totowka.translator.data.repository.TranslatorRepositoryImpl
import ru.totowka.translator.domain.model.WordEntity
import java.io.IOException

class TranslationInteractorTest {
    lateinit var translationRepositoryImpl: TranslatorRepositoryImpl
    lateinit var translationInteractor: TranslationInteractor
    private val entityStub = WordEntity(1, "totowka", null)

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        translationRepositoryImpl = mockk()
        translationInteractor = TranslationInteractor(translationRepositoryImpl)
    }

    @Test
    fun `getTranslation is successful`() {
        // Arrange
        every { translationRepositoryImpl.getWordTranslation(WORD_TO_TRANSLATE) } returns Single.just(
            arrayListOf(
                entityStub
            )
        )
        val expected = arrayListOf(entityStub)

        // Act
        val actual = translationInteractor.getTranslation(WORD_TO_TRANSLATE).blockingGet()

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
        verify(exactly = 1) { translationRepositoryImpl.getWordTranslation(WORD_TO_TRANSLATE) }
    }

    @Test
    fun `getTranslation is error`() {
        // Arrange
        every { translationRepositoryImpl.getWordTranslation(WORD_TO_TRANSLATE) } returns Single.fromCallable {throw IOException()}
        val expected = arrayListOf(entityStub)

        // Act
        val actual = translationInteractor.getTranslation(WORD_TO_TRANSLATE).test().assertError(IOException::class.java)

        // Assert
        verify(exactly = 1) { translationRepositoryImpl.getWordTranslation(WORD_TO_TRANSLATE) }
    }

    companion object {
        private const val WORD_TO_TRANSLATE = "WORD_TO_TRANSLATE"
    }
}